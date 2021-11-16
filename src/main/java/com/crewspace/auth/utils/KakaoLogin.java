package com.crewspace.auth.utils;

import com.crewspace.auth.constants.CustomExceptionCode;
import com.crewspace.auth.exception.CustomException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@NoArgsConstructor
@Component
public class KakaoLogin {

    private String req = "https://kapi.kakao.com/v2/user/me";

    public String getEmail(String token) throws IOException {
        URL url = new URL(req);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + token);

        int status = conn.getResponseCode();
        if(status != 200){
            log.warn("카카오 소셜 로그인 - 유효하지 않은 Access token입니다.");
            throw new CustomException(CustomExceptionCode.UNAUTHORIZED_SOCIAL_ACCESS_TOKEN);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while((line = br.readLine())!= null){
            result += line;
        }

        br.close();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();

        if(hasEmail){
            return element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
        }else{
            log.warn("카카오 소셜 로그인 - 이메일이 제공되지 않았습니다.");
            throw new CustomException(CustomExceptionCode.EMAIL_NOT_FOUND);
        }

    }

}
