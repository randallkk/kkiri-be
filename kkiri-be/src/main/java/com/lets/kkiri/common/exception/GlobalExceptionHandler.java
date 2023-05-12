package com.lets.kkiri.common.exception;

import com.lets.kkiri.config.mm.MattermostManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.Enumeration;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private MattermostManager mattermostManager;

    @ExceptionHandler({KkiriException.class})
    protected ResponseEntity handleKkiriException(KkiriException e, HttpServletRequest req) {
        mattermostManager.sendNotification(e, req.getRequestURI(), getParams(req));
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ErrorResponse.toErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity exceptionTest(Exception e, HttpServletRequest req) {
        mattermostManager.sendNotification(e, req.getRequestURI(), getParams(req));
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getParams(HttpServletRequest req) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> keys = req.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            params.append("- ").append(key).append(" : ").append(req.getParameter(key)).append("\n");
        }

        return params.toString();
    }
    @ExceptionHandler({UnsupportedOperationException.class})
    protected ResponseEntity handleUnsupportedOperationException(UnsupportedOperationException e, HttpServletRequest req) {
        mattermostManager.sendNotification(e, req.getRequestURI(), getParams(req));
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({FileNotFoundException.class})
    protected ResponseEntity handleFileNotFoundException(FileNotFoundException e, HttpServletRequest req) {
        mattermostManager.sendNotification(e, req.getRequestURI(), getParams(req));
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}