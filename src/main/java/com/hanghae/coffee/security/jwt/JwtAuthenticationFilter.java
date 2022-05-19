package com.hanghae.coffee.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        String token = jwtTokenProvider.resolveToken(request);
        log.info("Authorization :: " + token);
        // 2. validateToken 으로 토큰 유효성 검사
        if (token != null) {
            if (jwtTokenProvider.validateToken(token)) {
                // (추가) Redis 에 해당 accessToken logout 여부 확인
                String isLogout = jwtTokenProvider.logoutTokenCheck(token);
                if (ObjectUtils.isEmpty(isLogout)) {
                    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                    jwtTokenProvider.setAuthentication(token);
                } else {
                    jwtTokenProvider.setNullAuthentication();
                    request.setAttribute("EXCEPTION", "NOT LOGIN STATUS");
                }
            } else if (!jwtTokenProvider.validateToken(token)) {

                request.setAttribute("EXCEPTION", "NOT VALIDATE TOKEN");

            }

        } else {

            jwtTokenProvider.setNullAuthentication();
            request.setAttribute("EXCEPTION", "NOT LOGIN STATUS");

        }

        chain.doFilter(request, response);
    }

}