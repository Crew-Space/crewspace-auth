package com.crewspace.auth.entity;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    KAKAO("kakao", (attributes) -> {
        Map<String, Object> kakaoAccount =  (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> properties =  (Map<String, Object>) attributes.get("properties");

        // map의 요소를 그대로 가져오면 안되고, String class의 valueOf 활용
        return new UserProfile(
            String.valueOf(attributes.get("id")),
            String.valueOf(kakaoAccount.get("email")),
            String.valueOf(properties.get("thumbnail_image")),
            String.valueOf(properties.get("nickname"))
        );
    });

    private final String registrationId;
    private final Function<Map<String, Object>, UserProfile> of;

    OAuthAttributes(String registrationId, Function<Map<String, Object>, UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
            .filter(provider -> registrationId.equals(provider.registrationId))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new)
            .of.apply(attributes);
    }

}
