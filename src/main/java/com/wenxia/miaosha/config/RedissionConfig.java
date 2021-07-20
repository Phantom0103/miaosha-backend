package com.wenxia.miaosha.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-07-05
 */
@Configuration
public class RedissionConfig {

    @Value("${spring.redis.redisson.address}")
    private String address;

    @Value("${spring.redis.redisson.password}")
    private String password;

    @Value("${spring.redis.redisson.idleConnectionTimeout:10000}")
    private int idleConnectionTimeout;

    @Value("${spring.redis.redisson.connectTimeout:10000}")
    private int connectTimeout;

    @Value("${spring.redis.redisson.timeout:3000}")
    private int timeout;

    @Value("${spring.redis.redisson.retryInterval:3000}")
    private int retryInterval;

    @Value("${spring.redis.redisson.nettyThreads:64}")
    private int nettyThreads;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 单机模式
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password)
                .setIdleConnectionTimeout(idleConnectionTimeout)
                .setConnectTimeout(connectTimeout)
                .setTimeout(timeout)
                .setRetryInterval(retryInterval);
        config.setCodec(new JsonJacksonCodec())
                .setNettyThreads(nettyThreads);

        return Redisson.create(config);
    }
}
