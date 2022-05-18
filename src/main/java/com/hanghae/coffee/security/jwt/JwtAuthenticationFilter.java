package com.hanghae.coffee.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        // 1. Request Header 에서 JWT 토큰 추출
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        log.info("Authorization :: ", accessToken);
        // 2. validateToken 으로 토큰 유효성 검사
        if (accessToken != null) {
            if (jwtTokenProvider.validateToken(accessToken)) {
                // (추가) Redis 에 해당 accessToken logout 여부 확인
                String isLogout = jwtTokenProvider.logoutTokenCheck(accessToken);
                if (ObjectUtils.isEmpty(isLogout)) {
                    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                    this.setAuthentication(accessToken);
                } else {
                    this.setNullAuthentication();
                    request.setAttribute("EXCEPTION", "NOT LOGIN STATUS");
                }
            } else if (!jwtTokenProvider.validateToken(accessToken) && refreshToken != null) {

                // 리프레시 토큰 유효성 검증
                if (jwtTokenProvider.validateToken(refreshToken)) {

                    // refreshToken으로 user 정보 가져오기
                    String authId = jwtTokenProvider.getAuthId(refreshToken);
                    // 새로운 accessToken 발급
                    String newAccessToken = jwtTokenProvider.createAccessToken(authId);
                    // 헤더에 어세스 토큰 추가
                    jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
                    // 컨텍스트에 넣기
                    this.setAuthentication(newAccessToken);

                    // Refresh 토큰 삭제
                    jwtTokenProvider.deleteRefreshToken(authId);
                    // Refresh 토큰 생성
                    String newRefreshToken = jwtTokenProvider.createRefreshToken(authId);
                    // Refresh 토큰 헤더 세팅
                    jwtTokenProvider.setHeaderRefreshToken(response, newRefreshToken);
                    // Refresh 토큰 생성
                    jwtTokenProvider.saveRefreshToken(authId, newRefreshToken);

                } else {
                    request.setAttribute("EXCEPTION", "NOT VALIDATE REFRESH TOKEN");
                }
                // Access 토큰 만료 및 refresh 토큰 없을 때
            } else if (!jwtTokenProvider.validateToken(accessToken) && refreshToken == null) {

                request.setAttribute("EXCEPTION", "NOT VALIDATE ACCESS TOKEN");

            }

        } else {

            this.setNullAuthentication();
            request.setAttribute("EXCEPTION", "NOT EXIST ACCESS TOKEN");

        }
        chain.doFilter(request, response);
    }

    // SecurityContext 에 Authentication 객체를 저장합니다.
    private void setAuthentication(String token) {
        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setNullAuthentication() {

        SecurityContextHolder.getContext().setAuthentication(null);

    }
}
