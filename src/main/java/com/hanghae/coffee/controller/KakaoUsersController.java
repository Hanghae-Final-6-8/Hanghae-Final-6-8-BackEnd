package com.hanghae.coffee.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coffee.service.KakaoUsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Api(tags = {"KAKAO API"})
public class KakaoUsersController {

    private final KakaoUsersService kakaoUsersService;

    @GetMapping("/kakao")
    @ApiOperation("KAKAO USER 로그인")
    public String kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        System.out.println("code :: " + code);
        kakaoUsersService.kakaoLogin(code);
//        return "redirect:/";
        return null;
    }

}
