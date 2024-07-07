package com.lets.kkiri.common.exception;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    @Autowired
//    private MattermostManager mattermostManager;
//
//    @ExceptionHandler({KkiriException.class})
//    protected ResponseEntity handleKkiriException(KkiriException e, HttpServletRequest req) {
//        mattermostManager.sendNotification(e, req.getRequestURI(), getParams(req));
//        return ResponseEntity.status(e.getErrorCode().getStatus()).body(ErrorResponse.toErrorResponse(e.getErrorCode()));
//    }

    private String getParams(HttpServletRequest req) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> keys = req.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            params.append("- ").append(key).append(" : ").append(req.getParameter(key)).append("\n");
        }

        return params.toString();
    }
//    @ExceptionHandler({UnsupportedOperationException.class})
//    protected ResponseEntity handleUnsupportedOperationException(UnsupportedOperationException e, HttpServletRequest req) {
//        mattermostManager.sendNotification(e, req.getRequestURI(), getParams(req));
//        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler({FileNotFoundException.class})
//    protected ResponseEntity handleFileNotFoundException(FileNotFoundException e, HttpServletRequest req) {
//        mattermostManager.sendNotification(e, req.getRequestURI(), getParams(req));
//        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}