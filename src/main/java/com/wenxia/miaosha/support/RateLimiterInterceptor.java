package com.wenxia.miaosha.support;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author zhouw
 * @Description 接口限流拦截器（令牌桶）
 * @Date 2021-05-11
 */
public class RateLimiterInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;

    private static final String RESPONSE_STR = "{\"code\": -1,\"message\": \"抢购失败\"}";

    public RateLimiterInterceptor(RateLimiter rateLimiter) {
        super();
        this.rateLimiter = rateLimiter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (this.rateLimiter.tryAcquire()) {
            return true;
        }

        response.setContentType("application/json");
        ServletOutputStream out = response.getOutputStream();
        out.write(RESPONSE_STR.getBytes());
        out.flush();
        out.close();

        return false;
    }
}
