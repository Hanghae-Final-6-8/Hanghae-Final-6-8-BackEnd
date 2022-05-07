package com.hanghae.coffee.model;

import lombok.AllArgsConstructor;

public enum OauthType {
    GOOGLE("GOOGLE"), KAKAO("KAKAO"), NAVER("NAVER");

    private final String value;

    OauthType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;

    }

}
