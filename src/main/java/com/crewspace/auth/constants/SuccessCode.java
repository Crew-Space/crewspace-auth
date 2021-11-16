package com.crewspace.auth.constants;

import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {

    TEST_SUCCESS(OK, "성공~");

    private final HttpStatus status;
    private final String msg;
}
