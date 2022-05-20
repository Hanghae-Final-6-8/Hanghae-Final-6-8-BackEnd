package com.hanghae.coffee.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.coffee.dto.oauthProperties.UserInfoDto;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.service.users.users.UsersRepository;
import com.hanghae.coffee.security.jwt.JwtTokenProvider;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    public Users doLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 구글 API 호출
        UserInfoDto googleUserInfo = requestUserInfo(accessToken);

        // 3. 필요시에 회원가입
        Users googleUser = registerGoogleUserIfNeeded(googleUserInfo);


        return googleUser;
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

        public UserInfoDto requestUserInfo (String access_token) throws JsonProcessingException {
                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper mapper = new ObjectMapper();

            try {
                URL url = new URL(GOOGLE_SNS_USER_INFO_URL);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

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
        }


        private Users registerGoogleUserIfNeeded (UserInfoDto userInfoDto){
            // DB 에 중복된 Kakao Id 가 있는지 확인
            String googleId = userInfoDto.getAuthId();
            Users googleUsers = usersRepository.findAllByAuthId(googleId)
                .orElse(null);

            if (googleUsers == null) {
                // 회원가입
                googleUsers = Users.createUsers(userInfoDto);
                usersRepository.save(googleUsers);

            } else {
                googleUsers = Users.updateUsers(googleUsers, userInfoDto);

            }
            return googleUsers;
        }
    }
