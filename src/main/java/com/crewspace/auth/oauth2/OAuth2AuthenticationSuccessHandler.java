package com.crewspace.auth.oauth2;

import static com.crewspace.auth.constants.SuccessCode.*;

import com.crewspace.auth.constants.SuccessCode;
import com.crewspace.auth.dto.LoginSuccessResponse;
import com.crewspace.auth.dto.TokenDTO;
import com.crewspace.auth.jwt.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        TokenDTO token = tokenProvider.generateTokenDTO(authentication);
        setResponse(response, LOGIN_SUCCESS, token);
        return;
    }

    private void setResponse(HttpServletResponse response, SuccessCode successCode, TokenDTO token) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String responseMsg = mapper.writeValueAsString(new LoginSuccessResponse(successCode.getMsg(), token));
        response.getWriter().write(responseMsg);
    }
}
