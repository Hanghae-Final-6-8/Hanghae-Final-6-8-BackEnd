package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.dto.users.CountInfoByUserResponseDto;
import com.hanghae.coffee.dto.users.UserInfoResponseDto;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.users.UsersService;
import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@Api(tags = {"USERS API"})
public class UsersController {

    private final UsersService usersService;

    @GetMapping(value = "/auth")
    public UserInfoResponseDto getUserAuth(@AuthenticationPrincipal UserDetailsImpl users) {

        return usersService.getUserAuth(users.getUsername());

    }

    @GetMapping(value = "/logout")
    public DefaultResponseDto doLogout(HttpServletRequest request,
        @AuthenticationPrincipal UserDetailsImpl users) {
        //TODO : @AuthenticationPrincipal 에 왜 값이 안들어오는지 체크해야함.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return usersService.doLogout(request, users.getUsername());

    }

    @PostMapping(value = "/delete")
    public DefaultResponseDto doUserDelete(@AuthenticationPrincipal UserDetailsImpl users) {

        return usersService.doUserDelete(users.getUser());

    }

    @GetMapping(value = "/info")
    public CountInfoByUserResponseDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl users) {

        return usersService.getCountInfoByUser(users.getUser());

    }

    @GetMapping(value = "/reissue")
    public DefaultResponseDto reissue(HttpServletResponse response,@AuthenticationPrincipal UserDetailsImpl users) {

        return usersService.reissue(response, users.getUsername());

    }

//    @PostMapping("/update")
//    public DefaultResponseDto doUserInfoUpdate(
//        @RequestParam("nickname") String nickname,
//        @RequestParam("imageFile") MultipartFile file) throws IOException {
//
//    }

}
