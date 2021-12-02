package com.example.cinema.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class MethodArgumentNotValidExceptionResult extends ExceptionResult{
    private final List<String> details;

    public MethodArgumentNotValidExceptionResult(String message, HttpStatus httpStatus, List<String> details) {
        super(message, httpStatus);
        this.details = details;
    }
}
