package com.hanghae.coffee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.coffee.dto.oauthProperties.UserInfoDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 캐싱시 문제가 발생하지 않도록 proxy false 설정
//@Proxy(lazy=false)
@Table(indexes = {
    @Index(name="idx_authid", columnList = "authId", unique = true)
})
public class Users extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String authId;

    private String email;

    private String nickname;

    @Column(length = 1000)
    private String profileUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthType oauthType;

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Posts> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    private void setAuthId(String authId) {
        this.authId = authId;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    private void setOauthType(OauthType oauthType) {
        this.oauthType = oauthType;
    }

    //==생성 메서드==//
    public static Users createUsers(UserInfoDto userInfoDto) {
        Users users = new Users();
        users.setAuthId(userInfoDto.getAuthId());
        users.setNickname(userInfoDto.getNickname());
        users.setEmail(userInfoDto.getEmail());
        users.setProfileUrl(userInfoDto.getProfileUrl());
        users.setOauthType(userInfoDto.getOauthType());
        return users;
    }

    //==생성 메서드==//
    public Users updateUsersProfile(String profileUrl, String nickname) {

        this.setNickname(nickname);
        this.setProfileUrl(profileUrl);

        return this;
    }

}
