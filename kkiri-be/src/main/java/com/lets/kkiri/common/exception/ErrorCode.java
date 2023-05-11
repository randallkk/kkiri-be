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
    UNVALID_TOKEN(HttpStatus.CONFLICT, "인증되지 않은 토큰입니다."),
    ACCESS_TOKEN_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "access token이 존재하지 않는 요청입니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "access token이 만료되었습니다. 재발급을 요청하세요."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.CONFLICT, "refresh token이 만료되었습니다. 다시 로그인해주세요."),
    JWT_ALGORITHM_NOT_SUPPORTED(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 알고리즘입니다."),
    JWT_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다."),
    JWT_INVALID_CLAIM(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 클레임입니다."),
    JWT_SIGNATURE_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT 서명 검증에 실패했습니다."),
    JWT_CREATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT 생성에 실패했습니다."),
    JWT_DECODE_FAILED(HttpStatus.UNAUTHORIZED, "JWT 디코딩에 실패했습니다."),
    JWT_VERIFICATION_FAILED(HttpStatus.UNAUTHORIZED, "JWT 검증에 실패했습니다."),

    // 404 NOT_FOUND 존재하지 않는 리소스 접근
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    MOIM_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 모임입니다."),

    // 200 OK
    SUCCESS(HttpStatus.OK, "정확해"),

    // 409 CONFLICT 이미 존재하는 리소스 접근

    // 500 INTERNAL_SERVER_ERROR 서버 내부 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");
    private final HttpStatus status;
    private final String message;
}