package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.beans.BeansResponseDto;
import com.hanghae.coffee.dto.taste.TasteRequestDto;
import com.hanghae.coffee.dto.taste.TasteResponseDto;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.taste.TasteService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/taste")
@Api(tags = {"TASTE API"})
public class TasteController {

    private final TasteService tasteService;

    /**
     * 유저의 커피 취향 조사 결과 확인
     */
    @PostMapping(value = "/tests")
    public BeansResponseDto doTasteByUser(@RequestBody TasteRequestDto tasteRequestDto, @AuthenticationPrincipal UserDetailsImpl users) {

        return tasteService.doTasteByUser(users.getUser(), tasteRequestDto);
    }

    @GetMapping(value = "/tests")
    public TasteResponseDto findTasteByUser(@AuthenticationPrincipal UserDetailsImpl users) {
//        @AuthenticationPrincipal UserDetailsImpl users

        return tasteService.findTasteByUser(users.getUser());

    }

}
