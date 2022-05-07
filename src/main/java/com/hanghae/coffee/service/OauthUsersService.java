package com.hanghae.coffee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coffee.model.OauthType;

public interface OauthUsersService {

    /**
     * 각 Social Login 페이지로 Redirect 처리할 URL Build
     * 사용자로부터 로그인 요청을 받아 Social Login Server 인증용 code 요청
     */
    String getOauthRedirectURL();

    /**
     * API Server로부터 받은 code를 활용하여 사용자 인증 정보 요청
     * @param code API Server 에서 받아온 code
     * @return API 서버로 부터 응답받은 Json 형태의 결과를 string으로 반환
     */
    String doLogin(String code) throws JsonProcessingException;

    /**
     * oAuth type 에 따른 서비스 이동
     * @return API 서버로 부터 응답받은 Json 형태의 결과를 string으로 반
     */
    default OauthType type() {
        if (this instanceof KakaoUsersService) {
            return OauthType.KAKAO;
        } else if (this instanceof NaverUsersService) {
            return OauthType.NAVER;
        } else if (this instanceof GoogleUsersService) {
            return OauthType.GOOGLE;
        } else {
            return null;
        }
    }

}
