package com.crewspace.auth.dto.res;

import com.crewspace.auth.dto.BaseResponse;
import com.crewspace.auth.dto.payload.TokenDTO;
import lombok.Getter;

@Getter
public class LoginSuccessResponse extends BaseResponse {
    private TokenDTO token;

    public LoginSuccessResponse(String msg, TokenDTO token) {
        super(true, msg);
        this.token = token;
    }

}
