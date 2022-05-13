package com.hanghae.coffee.dto.oauthProperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
//@Profile("local")
public class OauthKakaoPropertiesDto {

    public static String kakaoClientId;

    public static String kakaoOauthRequestUrl;

    public static String kakaoTokenUrl;

    public static String kakaoUserInfoUrl;

    public static String kakaoRedirectUrl;

    @Value("${oauth.kakao.client.id}")
    public void setKakaoClientId(String kakaoClientId) {
        this.kakaoClientId = kakaoClientId;
    }
    @Value("${oauth.kakao.oauth.request.url}")
    public void setKakaoOauthRequestUrl(String kakaoOauthRequestUrl) {
        this.kakaoOauthRequestUrl = kakaoOauthRequestUrl;
    }
    @Value("${oauth.kakao.token.url}")
    public void setKakaoTokenUrl(String kakaoTokenUrl) {
        this.kakaoTokenUrl = kakaoTokenUrl;
    }
    @Value("${oauth.kakao.user.info.url}")
    public void setKakaoUserInfoUrl(String kakaoUserInfoUrl) {
        this.kakaoUserInfoUrl = kakaoUserInfoUrl;
    }
    @Value("${oauth.kakao.redirect.url}")
    public void setKakaoRedirectUrl(String kakaoRedirectUrl) {
        this.kakaoRedirectUrl = kakaoRedirectUrl;
    }

}

