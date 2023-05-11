package com.lets.kkiri.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({KkiriException.class})
    protected ResponseEntity handleKkiriException(KkiriException e) {
        return new ResponseEntity(e.getErrorCode().getMessage(), HttpStatus.valueOf(e.getErrorCode().getStatus().value()));
    }

    @ExceptionHandler({UnsupportedOperationException.class})
    protected ResponseEntity handleUnsupportedOperationException(UnsupportedOperationException e) {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({FileNotFoundException.class})
    protected ResponseEntity handleFileNotFoundException(FileNotFoundException e) {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}