package com.crewspace.auth.exception;

import com.crewspace.auth.constants.CustomExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private final CustomExceptionCode exceptionCode;
}

