package com.crewspace.auth.dto;

import com.crewspace.auth.constants.CustomExceptionCode;
import com.crewspace.auth.constants.SuccessCode;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor
public class BaseResponse {

    private Boolean succcess;
    private String msg;
    private LocalDateTime timestamp = LocalDateTime.now();

    @Builder
    public BaseResponse(Boolean success, String msg) {
        this.succcess = success;
        this.msg = msg;
    }

    public static ResponseEntity<BaseResponse> toCustomErrorResponse(
        CustomExceptionCode exceptionCode) {
        return ResponseEntity
            .status(exceptionCode.getStatus())
            .body(
                BaseResponse.builder()
                    .success(false)
                    .msg(exceptionCode.getMsg())
                    .build()
            );
    }

    public static ResponseEntity<BaseResponse> toBasicErrorResponse(HttpStatus status, String msg) {
        return ResponseEntity
            .status(status)
            .body(
                BaseResponse.builder()
                    .success(false)
                    .msg(msg)
                    .build()
            );
    }

    public static ResponseEntity<BaseResponse> toSuccessResponse(SuccessCode successCode) {
        return ResponseEntity
            .status(successCode.getStatus())
            .body(
                BaseResponse.builder()
                    .success(true)
                    .msg(successCode.getMsg())
                    .build()
            );
    }

}
