package com.company.urlshortenermain.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ExceptionResponse {
    private int code;
    private String message;
}
