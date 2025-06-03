package com.gardrops.imageupload.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.rate-limit.requests-per-minute}")
    private int requestsPerMinute;

    public RateLimitInterceptor(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        String key = "rate_limit:" + clientIp;

        String currentCount = redisTemplate.opsForValue().get(key);
        if (currentCount == null) {
            redisTemplate.opsForValue().set(key, "1", 1, TimeUnit.MINUTES);
            return true;
        }

        int count = Integer.parseInt(currentCount);
        if (count >= requestsPerMinute) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }

        redisTemplate.opsForValue().increment(key);
        return true;
    }
} 