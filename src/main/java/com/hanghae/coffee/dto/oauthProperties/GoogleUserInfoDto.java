package com.hanghae.coffee.dto.oauthProperties;

import com.hanghae.coffee.model.OauthType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class GoogleUserInfoDto extends UserInfoDto{

    public GoogleUserInfoDto(String authId, String nickname, String email, String profileUrl) {
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
        return OauthType.GOOGLE;
    }
}
