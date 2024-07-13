package com.lets.kkiri.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 값을 확인해줭"),

    // 401 UNAUTHORIZED 인증되지 않은 사용자 접근
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "서명이 유효하지 않은 토큰입니다."),
    ACCESS_TOKEN_NULL(HttpStatus.UNAUTHORIZED, "access token이 존재하지 않는 요청입니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "access token이 만료되었습니다. 재발급을 요청하세요."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "refresh token이 만료되었습니다. 다시 로그인해주세요."),

    // 404 NOT_FOUND 존재하지 않는 리소스 접근
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    MOIM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 모임입니다."),

    // 200 OK
    SUCCESS(HttpStatus.OK, "정확해"),

    // 422 UNPROCESSABLE_ENTITY 유효성 검사 실패
    MOIM_MEMBER_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY, "모임원을 추가해주세요."),

    // 500 INTERNAL_SERVER_ERROR 서버 내부 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    MOIM_CREATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "모임 생성에 실패했습니다.");
    private final HttpStatus status;
    private final String message;
}