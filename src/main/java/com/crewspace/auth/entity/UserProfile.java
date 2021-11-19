package com.crewspace.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfile {

    private String oauthId;
    private String email;
    private String image;
    private String nickname;

    @Builder
    public UserProfile(String oauthId, String email, String image, String nickname) {
        this.oauthId = oauthId;
        this.email = email;
        this.image = image;
        this.nickname = nickname;
    }

    public Member toMember(){
        return Member.builder()
            .authority(Authority.ROLE_USER)
            .oauthId(this.oauthId)
            .email(this.email)
            .image(this.image)
            .nickname(this.nickname)
            .build();
    }
}
