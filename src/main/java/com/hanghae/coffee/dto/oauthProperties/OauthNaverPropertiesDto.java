package com.hanghae.coffee.dto.oauthProperties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OauthNaverPropertiesDto {

    public static String naverClientId;

    public static String naverClientSecret;

    public static String naverOauthRequestUrl;

    public static String naverTokenUrl;

    public static String naverUserInfoUrl;

    public static String naverRedirectUrl;

    @Value("${oauth.naver.client.id}")
    public void setNaverClientId(String naverClientId) {
        this.naverClientId = naverClientId;
    }
    @Value("${oauth.naver.client.secret}")
    public void setNaverClientSecret(String naverClientSecret) {
        this.naverClientSecret = naverClientSecret;
    }
    @Value("${oauth.naver.oauth.request.url}")
    public void setNaverOauthRequestUrl(String naverOauthRequestUrl) {
        this.naverOauthRequestUrl = naverOauthRequestUrl;
    }
    @Value("${oauth.naver.token.url}")
    public void setNaverTokenUrl(String naverTokenUrl) {
        this.naverTokenUrl = naverTokenUrl;
    }
    @Value("${oauth.naver.user.info.url}")
    public void setNaverUserInfoUrl(String naverUserInfoUrl) {
        this.naverUserInfoUrl = naverUserInfoUrl;
    }
    @Value("${oauth.naver.redirect.url}")
    public void setNaverRedirectUrl(String naverRedirectUrl) {
        this.naverRedirectUrl = naverRedirectUrl;
    }

}
