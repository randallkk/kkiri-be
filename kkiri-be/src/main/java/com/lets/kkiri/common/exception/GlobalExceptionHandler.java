package com.lets.kkiri.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({KkiriException.class})
    protected ResponseEntity handleKkiriException(KkiriException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ErrorResponse.toErrorResponse(e.getErrorCode()));
    }
    @ExceptionHandler({UnsupportedOperationException.class})
    protected ResponseEntity handleUnsupportedOperationException(UnsupportedOperationException e) {
        e.printStackTrace();
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({FileNotFoundException.class})
    protected ResponseEntity handleFileNotFoundException(FileNotFoundException e) {
        e.printStackTrace();
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}