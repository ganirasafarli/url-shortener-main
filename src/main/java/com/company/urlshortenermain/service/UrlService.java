package com.company.urlshortenermain.service;

import com.company.urlshortenermain.controller.exception.InvalidUrlException;
import com.company.urlshortenermain.exception.NotFoundException;
import com.company.urlshortenermain.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public String shortenUrl(String originalUrl) {
        if (StringUtils.hasText(originalUrl)) {
            String cachedShortenedUrl = redisTemplate.opsForValue().get(originalUrl);
            if (StringUtils.hasText(cachedShortenedUrl)) {
                return cachedShortenedUrl;
            }
           else if (urlRepository.existsByUrl(originalUrl)) {
                return urlRepository.retrieveShortenedUrl(originalUrl);
            }
            String shortenedUrl = createShortUrl(originalUrl);
            urlRepository.saveUrl(originalUrl, shortenedUrl);
            saveUrlToRedis(originalUrl, shortenedUrl);
            return shortenedUrl;
        } else {
            throw new InvalidUrlException();
        }
    }

    @Async
    public void saveUrlToRedis(String shortenedUrl, String originalUrl) {
        redisTemplate.opsForValue().set(shortenedUrl, originalUrl);
    }

    public String retrieveOriginalUrl(String shortenedUrl) {
        String cachedUrl = redisTemplate.opsForValue().get(shortenedUrl);
        if (StringUtils.hasText(cachedUrl)) {
            return cachedUrl;
        }
        String url = urlRepository.retrieveUrl(shortenedUrl);
        if (StringUtils.hasText(url)) {
            saveUrlToRedis(shortenedUrl, url);
            return url;
        } else {
            throw new NotFoundException("Url not found exception");
        }
    }

    public String createShortUrl(String originalUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(originalUrl.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
