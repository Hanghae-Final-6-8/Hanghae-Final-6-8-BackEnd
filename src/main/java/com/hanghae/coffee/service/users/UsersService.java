package com.hanghae.coffee.service.users;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.dto.users.CountInfoByUserDto;
import com.hanghae.coffee.dto.users.CountInfoByUserResponseDto;
import com.hanghae.coffee.dto.users.UserAuthDto;
import com.hanghae.coffee.dto.users.UserInfoResponseDto;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.security.jwt.JwtTokenProvider;
import com.hanghae.coffee.service.users.users.UsersRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UsersRepository usersRepository;

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
    public DefaultResponseDto reissue(HttpServletResponse response, String authId) {

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


    public UserInfoResponseDto getUserAuth(Long userId) {

        UserAuthDto userAuthDto = usersRepository.getUserAuth(userId);

        return UserInfoResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(userAuthDto)
            .build();
    }

    public CountInfoByUserResponseDto getCountInfoByUser(Users users) {

        CountInfoByUserDto countInfoByUserDto = usersRepository.getCountInfoByUser(users.getId());

        return CountInfoByUserResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(countInfoByUserDto)
            .build();

    }

    public DefaultResponseDto doUserInfoUpdate(Users users, String url, String nickname) {

        Users.updateUsersProfile(users, url, nickname);

        return DefaultResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .build();
    }

}
