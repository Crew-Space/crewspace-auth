package com.crewspace.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String oauthId;

    private String email;
    private String image;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String oauthId, String email, String image, String nickname,
        Authority authority) {
        this.oauthId = oauthId;
        this.email = email;
        this.image = image;
        this.nickname = nickname;
        this.authority = authority;
    }

    public Member update(String email, String image, String nickname){
        this.email = email;
        this.image = image;
        this.nickname = nickname;
        return this;
    }
}
