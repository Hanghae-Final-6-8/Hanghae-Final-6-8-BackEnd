package com.hanghae.coffee.dto.oauthProperties;

import com.hanghae.coffee.model.OauthType;

public class KakaoUserInfoDto extends UserInfoDto {

    public KakaoUserInfoDto(String authId, String nickname, String email, String profileUrl) {
        super(authId, nickname, email, profileUrl);
    }

    @Override
    public String getAuthId() {
        return authId;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public OauthType getOauthType() {
        return OauthType.KAKAO;
    }


}
