package com.hanghae.coffee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanghae.coffee.dto.oauthProperties.GoogleUserInfoDto;
import com.hanghae.coffee.dto.oauthProperties.UserInfoDto;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.UsersRepository;
import com.hanghae.coffee.security.jwt.JwtTokenProvider;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class GoogleUsersService implements OauthUsersService {

    @Value("${oauth.google.url}")
    private String GOOGLE_SNS_BASE_URL;
    @Value("${oauth.google.client.id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${oauth.google.callback.url}")
    private String GOOGLE_SNS_CALLBACK_URL;
    @Value("${oauth.google.client.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${oauth.google.token.url}")
    private String GOOGLE_SNS_TOKEN_BASE_URL;
    @Value("${oauth.google.scope}")
    private String GOOGLE_SNS_SCOPE;
    @Value("${oauth.google.user.info.url}")
    private String GOOGLE_SNS_USER_INFO_URL;

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String,String> redisTemplate;


    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
        params.put("scope", GOOGLE_SNS_SCOPE);
        params.put("response_type", "code");
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining("&"));

        return GOOGLE_SNS_BASE_URL + "?" + parameterString;
    }

    @Override
    public String doLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 구글 API 호출
        UserInfoDto googleUserInfo = requestUserInfo(accessToken);

        // 3. 필요시에 회원가입
        Users googleUser = registerGoogleUserIfNeeded(googleUserInfo);


        return accessToken;
    }

    @Override
    public String doLogout(String accessToken, String refreshToken) throws JsonProcessingException {
        // 1. "액세스 토큰" 유효성 검사
//        if (!JwtTokenProvider.validateToken(accessToken)){
//            return "잘못된 요청입니다.";
//        }

        // 2. "액세스 토큰"에서 사용자 이메일 획득
        String usersEmail = jwtTokenProvider.getEmail(accessToken);

            // 3. Redis 에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.


        if (redisTemplate.opsForValue().get("RT:" + usersEmail) != null) {
                // 토큰 삭제 전 유효시간 가지고 오기
                Long expiration = jwtTokenProvider.getExpiration(accessToken);

                // Refresh Token 삭제
                redisTemplate.delete("RT:" + usersEmail);


            // 4. 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
            redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);


        // 3. Redis에 해당 email로 저장된 토큰이 있을 경우 삭제
        UserInfoDto googleUserInfo = requestUserInfo(accessToken);

        // 4. 해당 토큰 Blacklist에 추가(유효시간 동안)
        Users googleUser = registerGoogleUserIfNeeded(googleUserInfo);

        }
        return accessToken;
    }

    @Override
    public String reissue(String refreshToken)throws JsonProcessingException {
        // 1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return "Refresh Token 정보가 유효하지 않습니다.";
        }
        // 2. "리프레시 토큰"에서 사용자 이메일 획득
        String usersEmail = jwtTokenProvider.getEmail(refreshToken);

        Users googleUsers = usersRepository.findAllByRequestToken(refreshToken)
            .orElse(null);
        if (googleUsers != null) {
            // 3. Redis 에서 User email 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
            String Token = (String) redisTemplate.opsForValue().get("RT:" + usersEmail);

            // (추가) 로그아웃되어 Redis 에 RefreshToken 이 존재하지 않는 경우 처리
            if (ObjectUtils.isEmpty(Token)) {
                return "잘못된 요청입니다.";
            }
            if (!Token.equals(refreshToken)) {
                return "Refresh Token 정보가 일치하지 않습니다.";
            }

            // 4. 새로운 토큰 생성
            // DB 에 중복된 Kakao Id 가 있는지 확인
            String googleId = googleUsers.getAuthId();
            String requestToken = null;
            if (googleUsers == null) {
                // 토큰 생성
                requestToken = jwtTokenProvider.createAccessToken(googleId);
                if (refreshToken != null) {
                    Long expiration = jwtTokenProvider.getExpiration(requestToken);

                    // 5. 엑세스 토큰 Redis 업데이트
                    redisTemplate.opsForValue()
                        .set("AT:", requestToken, expiration, TimeUnit.MILLISECONDS);

                    Optional<Users> googleUser = usersRepository.findAllByAuthId(googleId);
                    GoogleUserInfoDto userInfoDto = new GoogleUserInfoDto(
                        googleUser.get().getAuthId(),
                        googleUser.get().getName(),
                        googleUser.get().getEmail(),
                        googleUser.get().getProfileUrl()
                    );
                    googleUsers = Users.createUsers(userInfoDto, requestToken);
                    usersRepository.save(googleUsers);
                }
            }
        }
        return null;
    }

        private String getAccessToken (String code){
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> params = new HashMap<>();
            params.put("code", code);
            params.put("client_id", GOOGLE_SNS_CLIENT_ID);
            params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
            params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
            params.put("grant_type", "authorization_code");

            ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, params, String.class);
            ObjectMapper mapper = new ObjectMapper();

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, String> map = new HashMap<>();
                try {
                    map = mapper.readValue(responseEntity.getBody(), Map.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return map.get("access_token");
            }
            return "구글 로그인 요청 처리 실패";
        }

        public UserInfoDto requestUserInfo (String access_token){
            try {
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();

                URL url = new URL(GOOGLE_SNS_USER_INFO_URL);

                Map<String, Object> params = new HashMap<>();
                params.put("alt", "json");
                params.put("access_token", access_token);

                String parameterString = params.entrySet().stream()
                    .map(x -> x.getKey() + "=" + x.getValue())
                    .collect(Collectors.joining("&"));

                ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                    GOOGLE_SNS_USER_INFO_URL + "?" + parameterString, String.class);
                Map<String, String> map = new HashMap<>();
                try {
                    map = mapper.readValue(responseEntity.getBody(), Map.class);
                    return UserInfoFactory.getOAuth2UserInfo(OauthType.GOOGLE, map.get("id"),
                        map.get("name"), map.get("email"), map.get("picture"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            } catch (IOException e) {
                throw new IllegalArgumentException("구글 로그인 요청 처리 실패");
            }
        }


        private Users registerGoogleUserIfNeeded (UserInfoDto userInfoDto){
            // DB 에 중복된 Kakao Id 가 있는지 확인
            String googleId = userInfoDto.getAuthId();
            String accessToken = null;
            String refreshToken = null;
            Users googleUsers = usersRepository.findAllByAuthId(googleId)
                .orElse(null);
            if (googleUsers == null) {
                // 회원가입
                accessToken = jwtTokenProvider.createAccessToken(googleId);
                refreshToken = jwtTokenProvider.createRefreshToken(googleId);
                googleUsers = Users.createUsers(userInfoDto, accessToken);
                usersRepository.save(googleUsers);

            } else {
                googleUsers = Users.updateUsers(googleUsers, userInfoDto);
                // requestToken Redis 저장
                accessToken = googleUsers.getRequestToken();
            }
            // accessToken Redis 저장
            Long expiration = jwtTokenProvider.getExpiration(accessToken);
            redisTemplate.opsForValue()
                .set("AT:" + googleUsers.getEmail(), accessToken, expiration,
                    TimeUnit.MILLISECONDS);

            // refreshToken Redis 저장
            Long expiration2 = jwtTokenProvider.getExpiration(refreshToken);
            redisTemplate.opsForValue()
                .set("RT:" + googleUsers.getEmail(), refreshToken, expiration2,
                    TimeUnit.MILLISECONDS);
            return googleUsers;
        }
    }
