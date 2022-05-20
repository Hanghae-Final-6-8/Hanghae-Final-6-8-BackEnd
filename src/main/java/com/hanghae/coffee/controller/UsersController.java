package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.dto.users.CountInfoByUserResponseDto;
import com.hanghae.coffee.dto.users.UserInfoResponseDto;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.posts.FileService;
import com.hanghae.coffee.service.users.UsersService;
import io.swagger.annotations.Api;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@Api(tags = {"USERS API"})
public class UsersController {

    private final static String DIRECTORY_URL = "profile/images";

    private final UsersService usersService;
    private final FileService fileService;

    @GetMapping(value = "/auth")
    public UserInfoResponseDto getUserAuth(@AuthenticationPrincipal UserDetailsImpl users) {

        return usersService.getUserAuth(users.getUser().getId());

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
    public DefaultResponseDto reissue(HttpServletResponse response,
        @AuthenticationPrincipal UserDetailsImpl users) {

        return usersService.reissue(response, users.getUsername());

    }

    @PostMapping("/update")
    public DefaultResponseDto doUserInfoUpdate(
        @RequestParam("nickname") String nickname,
        @RequestParam(value = "imageFile", required = false) MultipartFile file,
        @AuthenticationPrincipal UserDetailsImpl users) throws IOException {

        Users user = users.getUser();
        String url = null;
        if (file != null) {

            url = fileService.updateFile(user.getId(), user.getProfileUrl(), file, DIRECTORY_URL);

        }

        return usersService.doUserInfoUpdate(user, url, nickname);
    }

}
