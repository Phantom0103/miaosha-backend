package com.wenxia.miaosha.config;

import com.google.common.util.concurrent.RateLimiter;
import com.wenxia.miaosha.support.RateLimiterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author zhouw
 * @Description
 * @Date 2021-05-11
 */
@Configuration
public class SeckillWebConfig implements WebMvcConfigurer {

    @Autowired
    private Environment environment;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 为秒杀接口配置限流拦截器
        registry.addInterceptor(seckillRateLimiterInterceptor()).addPathPatterns("/seckill/order");
    }

    /**
     * 秒杀接口限流拦截器
     *
     * @return
     */
    @Bean
    public RateLimiterInterceptor seckillRateLimiterInterceptor() {
        int permits = environment.getProperty("mb.limiter-permits", Integer.class, 1000);
        @SuppressWarnings("UnstableApiUsage")
        RateLimiter limiter = RateLimiter.create(permits);
        return new RateLimiterInterceptor(limiter);
    }
}
