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
    /* 404 */
    EMAIL_NOT_FOUND(NOT_FOUND, "[소셜 로그인] 이메일이 제공되지 않았습니다.");

    private final HttpStatus status;
    private final String msg;
}