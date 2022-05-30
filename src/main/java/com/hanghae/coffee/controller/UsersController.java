package com.hanghae.coffee.controller;

import com.hanghae.coffee.advice.ErrorCode;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.global.ResponseFormat;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.posts.FileService;
import com.hanghae.coffee.service.users.UsersService;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
@Slf4j
public class UsersController {

    private final static String DIRECTORY_URL = "profile/images";

    private final UsersService usersService;
    private final FileService fileService;

    /**
     * 유저 기본 인증 정보
     */
    @GetMapping(value = "/auth")
    public ResponseEntity<?> getUserAuth(@AuthenticationPrincipal UserDetailsImpl users) {

        ResponseFormat responseFormat = new ResponseFormat().of(
            usersService.getUserAuth(users.getUser().getId()), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * 유저 로그아웃
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<?> doLogout(HttpServletRequest request,
        @AuthenticationPrincipal UserDetailsImpl users) {
        usersService.doLogout(request, users.getUsername());
        ResponseFormat responseFormat = new ResponseFormat().of("로그아웃 되었습니다.");

        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * 유저 탈퇴
     */
    @PostMapping(value = "/delete")
    public ResponseEntity<?> doUserDelete(@AuthenticationPrincipal UserDetailsImpl users) {

        usersService.doUserDelete(users.getUser());
        ResponseFormat responseFormat = new ResponseFormat().of("탈퇴 되었습니다.");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * 유저 활동 정보
     */
    @GetMapping(value = "/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl users) {

        ResponseFormat responseFormat = new ResponseFormat().of(
            usersService.getCountInfoByUser(users.getUser()), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * access token, refresh token 재발급
     */
    @GetMapping(value = "/reissue")
    public ResponseEntity<?> reissue(HttpServletResponse response,
        @AuthenticationPrincipal UserDetailsImpl users) {

        usersService.reissue(response, users.getUsername());
        ResponseFormat responseFormat = new ResponseFormat().of("Access Token 재발급 성공");

        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * 유저 프로필 수정
     */
    @PostMapping("/update")
    @Validated
    public ResponseEntity<?> doUserInfoUpdate(
        @RequestParam(value = "nickname") String nickname,
        @RequestParam(value = "profile_url", required = false) Optional<MultipartFile> file,
        @AuthenticationPrincipal UserDetailsImpl users)
        throws MissingServletRequestParameterException {

        if(StringUtils.isEmpty(nickname)){
            throw new MissingServletRequestParameterException("nickname", "string");
        }

        Users user = users.getUser();
        String url = null;

        if (file.isPresent()) {
            try {
                url = fileService.updateFile(user.getId(), user.getProfileUrl(), file.get(),
                    DIRECTORY_URL);
            } catch (IOException e) {
                throw new RestException(ErrorCode.COMMON_BAD_REQUEST_400_FILE);
            }
        }

        ResponseFormat responseFormat = new ResponseFormat()
            .of(usersService.doUserInfoUpdate(user, url, nickname), "success");

        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

}
