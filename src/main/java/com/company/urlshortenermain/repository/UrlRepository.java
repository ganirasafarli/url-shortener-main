package com.company.urlshortenermain.repository;

import com.company.urlshortenermain.controller.dto.Url;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Integer> {
    @Modifying
    @Query(value = "INSERT INTO urls (url,shorten_url) VALUES (:url,:shortenedUrl)", nativeQuery = true)
    @Transactional
    void saveUrl(String url, String shortenedUrl);

    @Query(value = "select url from urls where shorten_Url=:shortUrl", nativeQuery = true)
    @Transactional
    String retrieveUrl(String shortUrl);

    @Query(value = "select COUNT(*) > 0 from urls where url=:url", nativeQuery = true)
    @Transactional
    boolean existsByUrl(String url);

    @Query(value = "select shorten_url from urls where url=:url", nativeQuery = true)
    @Transactional
    String retrieveShortenedUrl(String url);
}
