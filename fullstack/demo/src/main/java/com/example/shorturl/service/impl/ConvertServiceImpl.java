package com.example.shorturl.service.impl;

import com.example.shorturl.constant.CommonConstant;
import com.example.shorturl.dao.ConvertDao;
import com.example.shorturl.model.ConvertEntity;
import com.example.shorturl.service.ConvertService;
import com.example.shorturl.utils.BeanCopierUtils;
import com.example.shorturl.utils.ScaleConvertUtil;
import com.example.shorturl.vo.ConvertVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.redisson.api.*;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConvertServiceImpl implements ConvertService {

    @Autowired
    private ConvertDao convertDao ;

    @Autowired
    private RedissonClient redissonClient;

    private RMapCache<String, ConvertVO> shortUrlRMapCache;

    private RBloomFilter<Object> bloomFilter ;

    @PostConstruct
    public void init() {

        bloomFilter = redissonClient.getBloomFilter("SHORT_URL_BLOOM");
        //初始化布隆过滤器 TODO 需严格测试1亿调数据大概占用的空间以及误判率
        List<ConvertEntity> all = convertDao.findAll();
        if(!CollectionUtils.isEmpty(all)){
            all.forEach(entity->{
                bloomFilter.add(entity.getShotCode());
            });
        }

        MapOptions<String, ConvertVO> options = MapOptions.<String, ConvertVO>defaults()
                .loader( createMapLoader())
                .writer(createMapWriter())
                .writeMode(MapOptions.WriteMode.WRITE_BEHIND)
                .writeBehindDelay(5000)
                .writeBehindBatchSize(50);

        shortUrlRMapCache =  redissonClient.getMapCache("SHORT_URL_RMAP",options);
    }

    @Override
    public ConvertVO getShortCodeByFullUrl(String fullUrl) {

        // 判断url的合法性
        if(StringUtils.isEmpty(fullUrl) || !validateHttpUrl(fullUrl)) {
            return null;
        }

        ConvertEntity convertEntity = convertDao.findByFullUrl(fullUrl);

        if(convertEntity != null) {
            return BeanCopierUtils.convert(convertEntity,ConvertVO.class);
        }

        ConvertVO addConvertEntity= new ConvertVO();
        //分布式发号器
        RLongAdder shortCodeAdder = redissonClient.getLongAdder("short_code_number_sender");
        shortCodeAdder.increment();
        Long currentId = shortCodeAdder.sum();

        String shortUrlSuffix = ScaleConvertUtil.convert(currentId, CommonConstant.LENGTH_SHORT_URL);
        addConvertEntity.setFullUrl(fullUrl);
        addConvertEntity.setShotCode(CommonConstant.PRIFIX_SHORT_URL + shortUrlSuffix);
        //
        shortUrlRMapCache.put(shortUrlSuffix,addConvertEntity);
        bloomFilter.add(shortUrlSuffix);
        return BeanCopierUtils.convert(addConvertEntity,ConvertVO.class);
    }

    @Override
    public ConvertVO getFullUrlByShort(String shortCode) {
        // 判断url的合法性
        if(StringUtils.isEmpty(shortCode)) {
            return null;
        }
        //布隆过滤器器先检查
        if(bloomFilter.contains(shortCode)){
            return shortUrlRMapCache.get(shortCode);
        }

        return null;
    }

    /**
     * Write-Behind策略
     *  1 同步更新缓存和数据持久化，保持数据一致性
     *  2 避免缓存失效
     * @return
     */
    private MapWriter<String, ConvertVO> createMapWriter() {
        MapWriter<String, ConvertVO> mapWriter = new MapWriter<String, ConvertVO>() {

            @Override
            public void write(Map<String, ConvertVO> map) {
                List<ConvertEntity> collect = map.values().stream().map(convertVO ->
                        BeanCopierUtils.convert(convertVO, ConvertEntity.class)).collect(Collectors.toList());
                convertDao.saveAll(collect);
            }

            @Override
            public void delete(Collection<String> keys) {
                keys.forEach( shortCode->{
                    convertDao.deleteByShortUrl(shortCode);
                });
            }
        };
        return mapWriter;
    }

    /**
     * Read-Through 策略
     * 1 无需管理数据源和缓存，只需要将数据源的同步委托给缓存提供程序 Cache Provider 即可
     * 2 可以减少数据源上的负载，也对缓存服务的故障具备一定的弹性
     * 3 预热
     * @return
     */
    private MapLoader<String, ConvertVO> createMapLoader() {
        MapLoader<String, ConvertVO> mapLoader = new MapLoader<String, ConvertVO>() {

            @Override
            public ConvertVO load(String shortCode) {
                ConvertEntity shotCode = convertDao.findByShotCode(shortCode);
                return null != shotCode ? BeanCopierUtils.convert(shotCode,ConvertVO.class) : null;
            }

            @Override
            public Iterable<String> loadAllKeys() {
                //TODO 根据请求频率、加载热点数据，控制好加载到缓存数据的大小。
                List<ConvertEntity> convertEntities = convertDao.findAll();
                List<String> shortCodeList = new ArrayList<>();
                convertEntities.forEach(convertEntity -> {
                    shortCodeList.add(convertEntity.getShotCode());
                });
                return shortCodeList;
            }

        };
        return mapLoader;
    }

    private boolean validateHttpUrl (String url) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
