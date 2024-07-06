package com.company.urlshortenermain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    @Async
    public void saveUrlToRedis(String shortenedUrl, String originalUrl) {
        redisTemplate.opsForValue().set(shortenedUrl, originalUrl);
    }

    public String retrieveUrl(String url) {
        return redisTemplate.opsForValue().get(url);
    }
}

