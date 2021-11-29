package com.crewspace.auth.jwt;

import static com.crewspace.auth.constants.CustomExceptionCode.*;

import com.crewspace.auth.constants.CustomExceptionCode;
import com.crewspace.auth.dto.payload.TokenDTO;
import com.crewspace.auth.exception.CustomException;
import com.crewspace.auth.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Getter
public class TokenProvider {

    // at, rt 유효 기간 설정
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30min
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7days

    private final Key key;
    private final RedisUtil redisUtil;

    // jwt 생성 암호화 Key 값
    public TokenProvider(@Value("${jwt.secret}") String secretKey, RedisUtil redisUtil){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisUtil = redisUtil;
    }

    // Access Token 과 Refresh Token 을 생성
    public TokenDTO generateTokenDTO(Authentication authentication){
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        Long now = (new Date()).getTime();

        // AT 생성 -> 유저 역할, 이름
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String userEmail;
        // Refacotr 요소
        if(authentication.getPrincipal() instanceof OAuth2User){
            OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> kakaoAccount =  (Map<String, Object>) attributes.get("kakao_account");
            userEmail = String.valueOf(kakaoAccount.get("email"));
        }else{
            userEmail = authentication.getName();
        }

        String accessToken = Jwts.builder()
            .setSubject(userEmail)       // payload "sub": "name"
            .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
            .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
            .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
            .compact();

        // RT 생성 -> 단순 생성 날짜
        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();

        redisUtil.setDateExpire(userEmail, refreshToken,REFRESH_TOKEN_EXPIRE_TIME);

        return TokenDTO.builder()
            .grantType(BEARER_TYPE)
            .accessToken(accessToken)
            .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
            .refreshToken(refreshToken)
            .build();
    }

    // 토큰 정보 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw new CustomException(WRONG_TYPE_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new CustomException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw new CustomException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw new CustomException(WRONG_TOKEN);
        } catch(Exception e) {
            log.error("===========================================");
            log.error("JwtFilter - doFilterInternal() 오류 발생");
            log.error("Exception message : {}", e.getMessage());
            log.error("===========================================");
            throw new CustomException(UNKNOWN_ERROR);
        }
    }

    private Claims parseClaims(String accessToken){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            log.info("Catch exception");
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 속 내용(정보) 꺼내기
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // SecurityContext를 사용하기 위한 과정
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
