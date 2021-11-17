package com.crewspace.auth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfile {

    private String oauthId;
    private String email;

    @Builder
    public UserProfile(String oauthId, String email) {
        this.oauthId = oauthId;
        this.email = email;
    }

    public Member toMember(){
        return Member.builder()
            .authority(Authority.ROLE_USER)
            .oAuthId(this.oauthId)
            .email(this.email)
            .build();
    }
}
