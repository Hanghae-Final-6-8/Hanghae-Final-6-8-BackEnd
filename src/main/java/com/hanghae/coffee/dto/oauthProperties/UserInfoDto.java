package com.hanghae.coffee.dto.oauthProperties;

import com.hanghae.coffee.model.OauthType;

public abstract class UserInfoDto {

    protected String authId;
    protected String nickname;
    protected String email;
    protected String profileUrl;

    public UserInfoDto(String authId, String nickname, String email, String profileUrl){
        this.authId = authId;
        this.nickname = nickname;
        this.email = email;
        this.profileUrl = profileUrl;

    }

    public abstract String getAuthId();

    public abstract String getNickname();

    public abstract String getEmail();

    public abstract String getProfileUrl();

    public abstract OauthType getOauthType();


}
