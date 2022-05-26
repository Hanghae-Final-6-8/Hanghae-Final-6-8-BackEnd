package com.hanghae.coffee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coffee.dto.global.ResponseFormat;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.service.oauth.OauthCommonService;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/login")
@Slf4j
@Api(tags = {"OAUTH API"})
public class OauthLoginController {

    private final OauthCommonService oauthCommonService;

    /**
     * 사용자로부터 SNS 로그인 요청을 oauthType 을 받아 처리
     *
     * @param oauthType (GOOGLE, KAKAO, NAVER)
     */
    @GetMapping(value = "/{oauth}")
    public String oauthLogin(
        @PathVariable(name = "oauth") String oauthType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} oauthLogin", oauthType);

        return oauthCommonService.request(OauthType.valueOf(oauthType.toUpperCase()));
    }

    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     *
     * @param oauthType (GOOGLE, KAKAO, NAVER)
     * @param code      API Server 로부터 넘어노는 code
     * @return ResponseEntity
     */
    @GetMapping(value = "/{oauth}/callback")
    public ResponseEntity<?> oauthLoginCallback(
        HttpServletResponse response,
        @PathVariable(name = "oauth") String oauthType,
        @RequestParam(name = "code") String code) throws JsonProcessingException {
        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);

        oauthCommonService.doLogin(response, OauthType.valueOf(oauthType.toUpperCase()), code);

        ResponseFormat responseFormat = new ResponseFormat().of("로그인 되었습니다.");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }


}
