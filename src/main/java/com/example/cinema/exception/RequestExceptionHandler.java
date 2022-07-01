package com.example.cinema.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class RequestExceptionHandler {

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ExceptionResult> handleRequestException(RequestException e) {
        ExceptionResult result = new ExceptionResult(e.getMessage(), HttpStatus.BAD_REQUEST);
        log.error("{}, status: {}, stackTrace: ", e.getMessage(), HttpStatus.BAD_REQUEST, e);
        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResult> handleRequestException(Exception e) {
        ExceptionResult result = new ExceptionResult(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("{}, status: {}, stackTrace: ", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        return ResponseEntity.internalServerError().body(result);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MethodArgumentNotValidExceptionResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        log.error("{}, status: {}, stackTrace: ", details, HttpStatus.BAD_REQUEST, e);
        MethodArgumentNotValidExceptionResult result = new MethodArgumentNotValidExceptionResult(e.getMessage(), HttpStatus.PRECONDITION_FAILED, details);
        return ResponseEntity.badRequest().body(result);
    }
}
