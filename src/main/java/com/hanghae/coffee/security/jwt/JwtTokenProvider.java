package com.hanghae.coffee.security.jwt;

import com.hanghae.coffee.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.net.InetAddress;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${secret.key}")
    private String SECRET_KEY;

    private static final String Authorization = "Authorization";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String BEARER_TYPE = "Bearer";

    // 토큰 유효시간 30분
    private final long ACCESS_TOKEN_VALID_TIME = 60 * 60 * 24 * 1000L;   // 24시간
    private final long REFRESH_TOKEN_VALID_TIME = 60 * 60 * 24 * 7 * 1000L;   // 1주

    private final UserDetailsService userDetailsService;
    private final RedisUtils redisUtils;

    // 객체 초기화, secretKey를 Base64로 인코딩한다
    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    // access 토큰 생성
    public String createAccessToken(String authId) {
        return this.createToken(authId, ACCESS_TOKEN_VALID_TIME);
    }

    // refresh 토큰 생성
    public String createRefreshToken(String authId) {
        return this.createToken(authId, REFRESH_TOKEN_VALID_TIME);
    }

    public String createToken(String authId, long tokenValid) {
        Claims claims = Jwts.claims().setSubject(authId); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        return Jwts.builder()
            .setClaims(claims) // 정보 저장
            .setIssuedAt(now) // 토큰 발행 시간 정보
            .setExpiration(new Date(now.getTime() + tokenValid)) // set Expire Time
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)  // 사용할 암호화 알고리즘과
            // signature 에 들어갈 secret값 세팅
            .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getAuthId(token));
        log.info("getAuthentication :: " + userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getAuthId(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Authorization);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 체크
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    //남은 만료일자 확인
    public Long getExpiration(String jwtToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody()
            .getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    // 어세스 토큰 헤더 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_TOKEN,accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader(REFRESH_TOKEN,refreshToken);
    }


    @Transactional(readOnly = true)
    public String logoutTokenCheck(String accessToken) {
        return redisUtils.getRedisData(accessToken);
    }

    @Transactional
    public void saveLogoutAccessToken(String accessToken) {
        Long expiration = getExpiration(accessToken);
        redisUtils.setDataExpire(accessToken,"logout",expiration);
    }

    @Transactional
    public void deleteRefreshToken(String authId) {
        String token = redisUtils.getRedisData("RT:" + authId);
        if(token != null){
            redisUtils.deleteData("RT:" + authId);
        }
    }

    @Transactional
    public void saveRefreshToken(String authId, String refreshToken) {
        Long expiration = getExpiration(refreshToken);
        // refreshToken Redis 저장
        redisUtils.setDataExpire("RT:" + authId, refreshToken, expiration);
    }

    // SecurityContext 에 Authentication 객체를 저장합니다.
    public void setAuthentication(String token) {
        // 토큰으로부터 유저 정보를 받아옵니다.
        Authentication authentication = getAuthentication(token);
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setNullAuthentication() {

        SecurityContextHolder.getContext().setAuthentication(null);

    }

}
