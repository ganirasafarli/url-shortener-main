package com.company.urlshortenermain.controller;

import com.company.urlshortenermain.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PutMapping("/shorten")
    public String shortenUrl(@RequestParam String url) {
        return urlService.shortenUrl(url);
    }

    @GetMapping("/retrieve")
    public String retrieveUrl(@RequestParam String shortUrl) {
        return urlService.retrieveOriginalUrl(shortUrl);
    }
}
