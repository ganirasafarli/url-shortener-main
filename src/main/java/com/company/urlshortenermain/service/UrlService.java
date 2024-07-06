package com.company.urlshortenermain.service;

import com.company.urlshortenermain.controller.exception.InvalidUrlException;
import com.company.urlshortenermain.dto.UrlResponse;
import com.company.urlshortenermain.exception.NotFoundException;
import com.company.urlshortenermain.repository.UrlRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisService redisService;

    @Transactional
    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 100))
    public UrlResponse shortenUrl(String originalUrl) {
        if (!StringUtils.hasText(originalUrl)) {
            throw new InvalidUrlException();
        }

        if (originalUrl.matches(".*\\d.*")) {
            throw new InternalException("Something bad happened.");
        }

        String cachedShortenedUrl = getOriginalUrlFromRedis(originalUrl);
        if (StringUtils.hasText(cachedShortenedUrl)) {
            return createUrlResponse(cachedShortenedUrl);
        } else if (urlRepository.existsByUrl(originalUrl)) {
            return createUrlResponse(urlRepository.retrieveShortenedUrl(originalUrl));
        }
        String shortenedUrl = createShortUrl(originalUrl);
        urlRepository.saveUrl(originalUrl, shortenedUrl);
        redisService.saveUrlToRedis(originalUrl, shortenedUrl);
        return createUrlResponse(shortenedUrl);
    }


    private UrlResponse createUrlResponse(String url) {
        return new UrlResponse(url);
    }

    public String getOriginalUrlFromRedis(String originalUrl) {
        return redisService.retrieveUrl(originalUrl);
    }

    public UrlResponse retrieveOriginalUrl(String shortenedUrl) {
        String cachedUrl = redisService.retrieveUrl(shortenedUrl);
        if (StringUtils.hasText(cachedUrl)) {
            return createUrlResponse(cachedUrl);
        }
        String url = urlRepository.retrieveUrl(shortenedUrl);
        if (StringUtils.hasText(url)) {
            redisService.saveUrlToRedis(shortenedUrl, url);
            return createUrlResponse(url);
        } else {
            throw new NotFoundException("This url has not been shortened.");
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
            return Long.toString(System.currentTimeMillis());
        }
    }
}
