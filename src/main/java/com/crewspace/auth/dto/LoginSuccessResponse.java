package com.crewspace.auth.dto;

import lombok.Getter;

@Getter
public class LoginSuccessResponse extends BaseResponse{
    private TokenDTO token;

    public LoginSuccessResponse(String msg, TokenDTO token) {
        super(true, msg);
        this.token = token;
    }

}
