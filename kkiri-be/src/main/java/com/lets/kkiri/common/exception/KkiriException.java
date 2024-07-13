package com.lets.kkiri.common.exception;

import lombok.Getter;
@Getter
public class KkiriException extends RuntimeException {
    private final ErrorCode errorCode;

    public KkiriException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public KkiriException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public KkiriException(ErrorCode errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }
}