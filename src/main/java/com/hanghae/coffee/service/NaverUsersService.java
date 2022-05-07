package com.hanghae.coffee.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public class NaverUsersService implements OauthUsersService{

    @Override
    public String getOauthRedirectURL() {
        return null;
    }

    @Override
    public String doLogin(String code) throws JsonProcessingException {

        return code;
    }
}
