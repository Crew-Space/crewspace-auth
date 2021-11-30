package com.crewspace.auth.oauth2;

import com.crewspace.auth.constants.CustomExceptionCode;
import com.crewspace.auth.dto.payload.TokenDTO;
import com.crewspace.auth.dto.req.ReIssueRequestDTO;
import com.crewspace.auth.entity.Member;
import com.crewspace.auth.entity.OAuthAttributes;
import com.crewspace.auth.entity.UserProfile;
import com.crewspace.auth.exception.CustomException;
import com.crewspace.auth.jwt.TokenProvider;
import com.crewspace.auth.repository.MemberRepository;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // social resousrce server 에서 가져온 유저 정보

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // OAuth 서비스 이름
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName(); // OAuth 로그인 시 key(PK)

        Map<String, Object> attributes = oAuth2User.getAttributes(); // OAuth 유저 정보

        UserProfile userProfile = OAuthAttributes.extract(registrationId, attributes);

        Member member = saveOrUpdate(userProfile);

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(member.getAuthority().toString())),
            attributes,
            userNameAttributeName
        );
    }

    private Member saveOrUpdate(UserProfile userProfile){
        Member member = memberRepository.findByOauthId(userProfile.getOauthId())
            .map(m -> m.update(userProfile.getEmail(), userProfile.getImage(), userProfile.getNickname()))
            .orElseGet(()->memberRepository.save(userProfile.toMember()));

        return member;
    }

    @Transactional
    public TokenDTO reissue(ReIssueRequestDTO request) {

        log.info("refresh token 검증");
        tokenProvider.validateToken(request.getRefreshToken());

        Authentication authentication = tokenProvider.getAuthentication(request.getAccessToken());

        log.info("로그인 되어있는 유저인지 체크");
        String refreshToken = tokenProvider.getRedisUtil().getData(authentication.getName())
            .orElseThrow(() -> new CustomException(CustomExceptionCode.UNAUTHORIZED_USER));

        log.info("유저의 refresh token값인지 체크");
        if(!request.getRefreshToken().equals(refreshToken)){
            throw new CustomException(CustomExceptionCode.UNAUTHORIZED_USER_REFRESH_TOKEN);
        }

        log.info("토큰 재발급");
        TokenDTO tokenDTO = tokenProvider.generateTokenDTO(authentication);

        return tokenDTO;
    }

    }
