package com.example.cinema.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ExceptionResult {
    private final String message;
    private final HttpStatus httpStatus;
    private final int statusCode;
    private final LocalDateTime time;

    public ExceptionResult(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
        this.time = LocalDateTime.now();
    }
}
