package com.company.urlshortenermain.service;

import ch.qos.logback.core.util.StringUtil;
import com.company.urlshortenermain.controller.exception.InvalidUrlException;
import com.company.urlshortenermain.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;

    public String shortenUrl(String originalUrl) {
        if (StringUtils.hasText(originalUrl)) {
            String shortenedUrl = cutUrl(originalUrl);
            urlRepository.saveUrl(originalUrl, shortenedUrl);
            return shortenedUrl;
        } else {
            throw new InvalidUrlException();
        }
    }

    public String cutUrl(String originalUrl) {
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
