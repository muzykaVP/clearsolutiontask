package com.example.clearsolutiontask.exception.handler;

import com.example.clearsolutiontask.exception.DataProcessingException;
import com.example.clearsolutiontask.exception.DateProcessingException;
import com.example.clearsolutiontask.exception.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ResponseException> catchDataProcessingException(DataProcessingException e) {
        return new ResponseEntity<>(new ResponseException(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseException> catchDateProcessingException(DateProcessingException e) {
        return new ResponseEntity<>(new ResponseException(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ResponseException> catchDateProcessingException(DuplicateKeyException e) {
        return new ResponseEntity<>(new ResponseException(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<ResponseException> catchDateProcessingException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(new ResponseException(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }
 }