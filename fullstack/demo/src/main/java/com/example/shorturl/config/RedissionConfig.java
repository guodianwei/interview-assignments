package com.example.shorturl.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author fengXiong
 * @Date 2021/3/15 2:46 下午
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissionConfig {

    private String host;
    private int port;
    private String password;
    private int database;
    private int connectionPoolSize = 60;
    private int maxIdle = 200000;
    private int maxWaitMillis = 15000;

    @Bean
    public Config config() {
        JsonJacksonCodec codec = new JsonJacksonCodec();
        Config config = new Config();
        String url = "redis://" + host + ":" + port;
        SingleServerConfig singleServerConfig = config
                .setCodec(codec)
                .useSingleServer()
                .setTimeout(maxWaitMillis)
                .setIdleConnectionTimeout(maxIdle)
                .setConnectionPoolSize(connectionPoolSize)
                .setAddress(url);
        if (StringUtils.isNotBlank(password)) {
            singleServerConfig.setPassword(password);
        }
        return config;
    }

    @Bean(destroyMethod="shutdown")
    public RedissonClient redisClient(Config config) {
        return Redisson.create(config);
    }
}
