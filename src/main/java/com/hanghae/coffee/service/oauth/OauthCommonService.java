package com.hanghae.coffee.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.dto.users.LoginResponseDto;
import com.hanghae.coffee.dto.users.LogoutResponseDto;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.users.UsersRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.security.jwt.JwtTokenProvider;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Builder.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthCommonService {

    private final List<OauthUsersService> oauthUsersServiceList;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;

    public String request(OauthType oauthType) {
        OauthUsersService oauthUsersService = this.findOauthByType(oauthType);
        String redirectURL = oauthUsersService.getOauthRedirectURL();
        return redirectURL;
    }

    public LoginResponseDto doLogin(HttpServletResponse response, OauthType oauthType, String code) throws JsonProcessingException {
        OauthUsersService oauthUsersService = this.findOauthByType(oauthType);
        Users users = oauthUsersService.doLogin(code);
        forceLogin(response, users);

        return LoginResponseDto.builder()
            .status(HttpStatus.OK)
            .msg("로그인 되었습니다.")
            .build();
    }

    @Transactional
    public DefaultResponseDto doLogout(HttpServletRequest request, String authId) {
        String token = jwtTokenProvider.resolveToken(request);
        jwtTokenProvider.deleteRefreshToken(authId);
        jwtTokenProvider.saveLogoutAccessToken(token);
        jwtTokenProvider.setNullAuthentication();
        return DefaultResponseDto.builder()
            .status(HttpStatus.OK)
            .msg("로그아웃 되었습니다.")
            .build();
    }


    @Transactional
    public DefaultResponseDto doUserDelete(Users users) {

        usersRepository.delete(users);
        jwtTokenProvider.deleteRefreshToken(users.getAuthId());
        jwtTokenProvider.setNullAuthentication();

        return DefaultResponseDto.builder()
            .status(HttpStatus.OK)
            .msg("탈퇴 되었습니다.")
            .build();
    }

    @Transactional
    public DefaultResponseDto reissue(HttpServletResponse response ,String authId) {

        // 새로운 accessToken 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(authId);
        // 헤더에 어세스 토큰 추가
        jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
        // 컨텍스트에 넣기
        jwtTokenProvider.setAuthentication(newAccessToken);

        // Refresh 토큰 삭제
        jwtTokenProvider.deleteRefreshToken(authId);
        // Refresh 토큰 생성
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authId);
        // Refresh 토큰 헤더 세팅
        jwtTokenProvider.setHeaderRefreshToken(response, newRefreshToken);
        // Refresh 토큰 생성
        jwtTokenProvider.saveRefreshToken(authId, newRefreshToken);

        return DefaultResponseDto.builder()
            .status(HttpStatus.OK)
            .msg("Access Token 재발급 성공")
            .build();

    }

    private String forceLogin(HttpServletResponse response, Users users){
        log.info("getAuthId :: {}, getNickname :: {} ",users.getAuthId() , users.getNickname());
        UserDetails userDetails = new UserDetailsImpl(users);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.createAccessToken(users.getAuthId());
        String refreshToken = jwtTokenProvider.createRefreshToken(users.getAuthId());
        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);

        jwtTokenProvider.saveRefreshToken(users.getAuthId(), refreshToken);

        return accessToken;

    }

    private OauthUsersService findOauthByType(OauthType oauthType) {
        return oauthUsersServiceList.stream()
            .filter(x -> x.type() == oauthType)
            .findFirst()
            .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, "알 수 없는 oAuthType 입니다."));
    }

}
