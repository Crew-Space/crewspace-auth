package com.crewspace.auth.constants;

import static org.springframework.http.HttpStatus.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomExceptionCode {

    /* 401 - 유효하지 않음 */
    UNAUTHORIZED_SOCIAL_ACCESS_TOKEN(UNAUTHORIZED, "[소셜 로그인] 유효하지 않은 Access token입니다."),
    UNAUTHORIZED_USER(UNAUTHORIZED, "로그아웃된 유저입니다"),
    UNAUTHORIZED_USER_REFRESH_TOKEN(UNAUTHORIZED, "유저의 리프레시 토큰이 아닙니다!"),
    // token 관련
    WRONG_TYPE_TOKEN(UNAUTHORIZED, "잘못된 JWT 서명을 가진 토큰입니다."),
    EXPIRED_TOKEN(UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    WRONG_TOKEN(UNAUTHORIZED, "JWT 토큰이 잘못되었습니다."),
    UNKNOWN_ERROR(UNAUTHORIZED, "알 수 없는 요청 인증 에러! 헤더에 토큰을 넣어 보냈는지 다시 한 번 확인해보세요."),
    ACCESS_DENIED(UNAUTHORIZED, "접근이 거절되었습니다."),

    /* 404 */
    EMAIL_NOT_FOUND(NOT_FOUND, "[소셜 로그인] 이메일이 제공되지 않았습니다."),;



    private final HttpStatus status;
    private final String msg;
}