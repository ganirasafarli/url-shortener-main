package com.company.urlshortenermain.controller.exception;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException() {
        super(String.format("Url is invalid"));
    }
}
