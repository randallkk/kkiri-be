package com.lets.kkiri.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KkiriException extends RuntimeException {
    private final ErrorCode errorCode;
}