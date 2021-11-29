package com.crewspace.auth.constants;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    LOGIN_SUCCESS(OK, "소셜 로그인 성공"),
    REISSUE_SUCCESS(OK, "토큰 재발급 성공"),
    TEST_SUCCESS(OK, "성공~");

    private final HttpStatus status;
    private final String msg;
}
