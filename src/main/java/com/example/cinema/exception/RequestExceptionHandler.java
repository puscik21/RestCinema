package com.example.cinema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ExceptionResult> handleRequestException(RequestException e) {
        ExceptionResult result = new ExceptionResult(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResult> handleRequestException(Exception e) {
        ExceptionResult result = new ExceptionResult(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentNotValidExceptionResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        MethodArgumentNotValidExceptionResult result = new MethodArgumentNotValidExceptionResult(e.getMessage(), HttpStatus.PRECONDITION_FAILED, details);
        return ResponseEntity.badRequest().body(result);
    }
}
