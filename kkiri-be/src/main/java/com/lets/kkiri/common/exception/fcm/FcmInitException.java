package com.lets.kkiri.common.exception.fcm;

import org.springframework.http.HttpStatus;

public class FcmInitException extends RuntimeException {
    public FcmInitException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}