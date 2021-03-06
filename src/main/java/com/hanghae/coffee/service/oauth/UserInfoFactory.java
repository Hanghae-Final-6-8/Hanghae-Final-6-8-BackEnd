package com.hanghae.coffee.service.oauth;

import com.hanghae.coffee.advice.ErrorCode;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.oauthProperties.GoogleUserInfoDto;
import com.hanghae.coffee.dto.oauthProperties.KakaoUserInfoDto;
import com.hanghae.coffee.dto.oauthProperties.NaverUserInfoDto;
import com.hanghae.coffee.dto.oauthProperties.UserInfoDto;
import com.hanghae.coffee.model.OauthType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoFactory {
    public static UserInfoDto getOAuth2UserInfo(OauthType oauthType, String id, String nickname, String email, String profileUrl) {
        switch (oauthType) {
            case GOOGLE: return new GoogleUserInfoDto(id, nickname, email, profileUrl);
            case NAVER: return new NaverUserInfoDto(id, nickname, email, profileUrl);
            case KAKAO: return new KakaoUserInfoDto(id, nickname, email, profileUrl);
            default: throw new RestException(ErrorCode.NOT_FOUND_OAUTH);
        }
    }
}
