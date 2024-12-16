package com.company.urlshortenermain.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(com.company.urlshortenermain.controller.exception.InvalidUrlException.class)
    public ResponseEntity<com.company.urlshortenermain.controller.exception.ExceptionResponse> handleInvalidUrlException(com.company.urlshortenermain.controller.exception.InvalidUrlException ex) {
        log.error(ex.getMessage());
        com.company.urlshortenermain.controller.exception.ExceptionResponse response = com.company.urlshortenermain.controller.exception.ExceptionResponse.of(400, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
