package com.company.urlshortenermain.controller;

import com.company.urlshortenermain.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PutMapping("/shorten")
    public String shortenUrl(@RequestParam String url) {
        return urlService.shortenUrl(url);
    }
}
