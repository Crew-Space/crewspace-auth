package com.crewspace.auth.dto.res;

import com.crewspace.auth.constants.SuccessCode;
import com.crewspace.auth.dto.BaseResponse;
import com.crewspace.auth.dto.payload.TokenDTO;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ReIssueResponse extends BaseResponse {

    TokenDTO data;

    private ReIssueResponse(Boolean success, String msg, TokenDTO data) {
        super(success, msg);
        this.data = data;
    }

    public static ReIssueResponse of(Boolean success, String msg, TokenDTO data){
        return new ReIssueResponse(success, msg, data);
    }

    public static ResponseEntity<ReIssueResponse> newResponse(SuccessCode code, TokenDTO data){
        ReIssueResponse response = ReIssueResponse.of(true, code.getMsg(), data);
        return new  ResponseEntity(response, code.getStatus());
    }
}