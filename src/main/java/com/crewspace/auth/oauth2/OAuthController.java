package com.crewspace.auth.oauth2;

import com.crewspace.auth.constants.SuccessCode;
import com.crewspace.auth.dto.payload.TokenDTO;
import com.crewspace.auth.dto.req.ReIssueRequestDTO;
import com.crewspace.auth.dto.res.ReIssueResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oauthService;

    @PostMapping
    @RequestMapping("/reissue")
    public ResponseEntity<ReIssueResponse> reissue(@RequestBody @Valid ReIssueRequestDTO request){
        TokenDTO response = oauthService.reissue(request);
        return ReIssueResponse.newResponse(SuccessCode.REISSUE_SUCCESS, response);
    }

}
