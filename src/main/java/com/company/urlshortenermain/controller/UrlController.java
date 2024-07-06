package com.company.urlshortenermain.controller;

import com.company.urlshortenermain.dto.UrlResponse;
import com.company.urlshortenermain.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PutMapping("/shorten")
    public ResponseEntity<UrlResponse> shortenUrl(@RequestParam String url) {
        return ResponseEntity.ok(urlService.shortenUrl(url));
    }

    @GetMapping("/retrieve")
    public ResponseEntity<UrlResponse> retrieveUrl(@RequestParam String shortUrl) {
        return ResponseEntity.ok(urlService.retrieveOriginalUrl(shortUrl));
    }
}
