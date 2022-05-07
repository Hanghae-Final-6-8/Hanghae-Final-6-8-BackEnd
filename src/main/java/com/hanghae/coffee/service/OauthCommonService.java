package com.hanghae.coffee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.model.OauthType;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthCommonService {

    private final List<OauthUsersService> oauthUsersServiceList;
    private final HttpServletResponse response;

    public void request(OauthType oauthType) {
        OauthUsersService oauthUsersService = this.findOauthByType(oauthType);
        String redirectURL = oauthUsersService.getOauthRedirectURL();
        try {
            response.sendRedirect(redirectURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String doLogin(OauthType oauthType, String code) throws JsonProcessingException {
        OauthUsersService oauthUsersService = this.findOauthByType(oauthType);
        return oauthUsersService.doLogin(code);
    }

    private OauthUsersService findOauthByType(OauthType oauthType) {
        return oauthUsersServiceList.stream()
            .filter(x -> x.type() == oauthType)
            .findFirst()
            .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, "알 수 없는 oAuthType 입니다."));
    }

}
