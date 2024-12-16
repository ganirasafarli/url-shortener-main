package com.company.urlshortenermain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableRetry
@EnableScheduling
public class UrlShortenerMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerMainApplication.class, args);
    }

}
