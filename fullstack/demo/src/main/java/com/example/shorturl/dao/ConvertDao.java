package com.example.shorturl.dao;

import com.example.shorturl.model.ConvertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvertDao extends JpaRepository<ConvertEntity, Long> {
    /**
     * 根据url获取对应的转换实体
     * @param url
     * @return
     */
    ConvertEntity findByFullUrl(String url);


    /**
     * 根据shortUrl获取对应的转换实体
     * @param shortUrl
     * @return
     */
    ConvertEntity findByShortCode(String shortUrl);

    /**
     * 根据shortUrl删除数据
     * @param shortUrl
     */
    void deleteByShortUrl (String shortUrl);

    /**
     * 获取已存储的URL总数，则该数值+1即为短链生成时所使用的那个最新id
     * @return
     */
    long count();
}
