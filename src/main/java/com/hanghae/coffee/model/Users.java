package com.hanghae.coffee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.coffee.dto.oauthProperties.UserInfoDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 캐싱시 문제가 발생하지 않도록 proxy false 설정
@Proxy(lazy=false)
public class Users extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String authId;

    private String email;

    private String nickname;

    private String profileUrl;

    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OauthType oauthType;

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Posts> posts = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "users")
    private List<Comments> comments = new ArrayList<>();

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
    public static Users updateUsers(Users users, UserInfoDto userInfoDto) {

        users.setNickname(userInfoDto.getNickname());
        users.setEmail(userInfoDto.getEmail());
        users.setProfileUrl(userInfoDto.getProfileUrl());
        users.setOauthType(userInfoDto.getOauthType());

        return users;
    }

    //==연관관계 메서드==//
    public void createPosts(Posts post) {
        posts.add(post);
        post.setUsers(this);
    }

}
