package com.hanghae.coffee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coffee.dto.users.LoginResponseDto;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.OauthCommonService;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@Slf4j
@Api(tags = {"LOGIN API"})
public class OauthLoginController {

    private final OauthCommonService oauthCommonService;

    /**
     * 사용자로부터 SNS 로그인 요청을 oauthType 을 받아 처리
     * @param oauthType (GOOGLE, KAKAO, NAVER)
     */
    @GetMapping(value = "/login/{oauth}")
    public void oauthLogin(
        @PathVariable(name = "oauth") String oauthType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} oauthLogin", oauthType);

        oauthCommonService.request(OauthType.valueOf(oauthType.toUpperCase()));
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param oauthType (GOOGLE, KAKAO, NAVER)
     * @param code API Server 로부터 넘어노는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */
    @GetMapping(value = "/login/{oauth}/callback")
    public LoginResponseDto oauthLoginCallback(
        HttpServletResponse response,
        @PathVariable(name = "oauth") String oauthType,
        @RequestParam(name = "code") String code) throws JsonProcessingException {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);
        return oauthCommonService.doLogin(response, OauthType.valueOf(oauthType.toUpperCase()), code);
    }

    @GetMapping(value = "/logout")
    public LoginResponseDto doLogout(HttpServletRequest request, @AuthenticationPrincipal UserDetailsImpl users){
        //TODO : @AuthenticationPrincipal 에 왜 값이 안들어오는지 체크해야함.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return oauthCommonService.doLogout(request ,users.getUsername());

    }

}
