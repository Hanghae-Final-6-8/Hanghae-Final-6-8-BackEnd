package com.hanghae.coffee.service.users;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.users.CountInfoByUserDto;
import com.hanghae.coffee.dto.users.UserAuthDto;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.security.jwt.JwtTokenProvider;
import com.hanghae.coffee.repository.users.UsersRepository;
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
    public void doLogout(HttpServletRequest request, String authId) {

        String token = jwtTokenProvider.resolveToken(request);
        jwtTokenProvider.deleteRefreshToken(authId);
        jwtTokenProvider.saveLogoutAccessToken(token);
        jwtTokenProvider.setNullAuthentication();

    }


    @Transactional
    public void doUserDelete(Users users) {

        usersRepository.delete(users);
        jwtTokenProvider.deleteRefreshToken(users.getAuthId());
        jwtTokenProvider.setNullAuthentication();

    }

    @Transactional
    public void reissue(HttpServletResponse response, String authId) {

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

    }


    public UserAuthDto getUserAuth(Long userId) {

        return usersRepository.getUserAuth(userId);

    }

    public CountInfoByUserDto getCountInfoByUser(Users users) {

        return usersRepository.getCountInfoByUser(users.getId());

    }

    @Transactional
    public void doUserInfoUpdate(Users users, String url, String nickname) {

        Users user = usersRepository.findAllByAuthId(users.getAuthId()).orElseThrow(
            () -> new RestException(HttpStatus.BAD_REQUEST,"해당 사용자를 찾을 수 없습니다.")
        );

        user.updateUsersProfile(url, nickname);

    }

}
