package com.hanghae.coffee.model;

public enum OauthType {
    GOOGLE(AuthType.GOOGLE),
    KAKAO(AuthType.KAKAO),
    NAVER(AuthType.NAVER);

    private final String type;

    OauthType(String type) {
        this.type = type;
    }

    public String getAuthType() {
        return this.type;
    }

    private static class AuthType {

        private static final String GOOGLE = "GOOGLE";
        private static final String KAKAO = "KAKAO";
        private static final String NAVER = "NAVER";
    }
}
