package com.hanghae.coffee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.coffee.dto.oauthProperties.GoogleUserInfoDto;
import com.hanghae.coffee.dto.oauthProperties.OauthPropertiesDto;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.UsersRepository;
import com.hanghae.coffee.security.jwt.JwtTokenProvider;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class GoogleUsersService implements OauthUsersService {

    @Value("${sns.google.url}")
    private String GOOGLE_SNS_BASE_URL;
    @Value("${sns.google.client.id}")
    private String GOOGLE_SNS_CLIENT_ID;
    @Value("${sns.google.callback.url}")
    private String GOOGLE_SNS_CALLBACK_URL;
    @Value("${sns.google.client.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;
    @Value("${sns.google.token.url}")
    private String GOOGLE_SNS_TOKEN_BASE_URL;
    @Value("${sns.google.scope}")
    private String GOOGLE_SNS_SCOPE;
    @Value("${sns.google.user.info.url}")
    private String GOOGLE_SNS_USER_INFO_URL;

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OauthPropertiesDto oauthPropertiesDto;

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
        GoogleUserInfoDto googleUserInfo = requestUserInfo(accessToken);

        // 3. 필요시에 회원가입
        Users googleUser = registerGoogleUserIfNeeded(googleUserInfo);

        return accessToken;
    }


    private String getAccessToken(String code) {
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
            Map<String, String> map = new HashMap<>() {};
            try{
                map = mapper.readValue(responseEntity.getBody(), Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return map.get("access_token");
        }
        return "구글 로그인 요청 처리 실패";
    }

    private Users registerGoogleUserIfNeeded(GoogleUserInfoDto googleUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String googleId = googleUserInfo.getId();
        Users googleUsers = usersRepository.findAllByAuthId(googleId)
            .orElse(null);
        if (googleUsers == null) {
            // 회원가입
            // username: kakao nickname
            String name = googleUserInfo.getName();

            // email: kakao email
            String email = googleUserInfo.getEmail();

            String requestToken = jwtTokenProvider.createRefreshToken(googleId);

            googleUsers = Users.createUser(name, email, googleId, OauthType.GOOGLE, requestToken);

            usersRepository.save(googleUsers);
        }

        return googleUsers;
    }

    public GoogleUserInfoDto requestUserInfo(String access_token) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper mapper = new ObjectMapper();

            URL url = new URL(GOOGLE_SNS_USER_INFO_URL);

            Map<String, Object> params = new HashMap<>();
            params.put("alt","json");
            params.put("access_token", access_token);


            String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));


            ResponseEntity<String> responseEntity = restTemplate.getForEntity(GOOGLE_SNS_USER_INFO_URL + "?" + parameterString, String.class);
            Map<String, String> map = new HashMap<>() {};
            try {
                map = mapper.readValue(responseEntity.getBody(), Map.class);
                System.out.println("=====구글 사용자 정보=====");
                System.out.println("id: " + map.get("id"));
                System.out.println("name: " + map.get("name"));
                System.out.println("email: " + map.get("email"));
                GoogleUserInfoDto googleUserInfoDto = new GoogleUserInfoDto(map.get("id"),map.get("name"),map.get("email"));
                return googleUserInfoDto;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        } catch (IOException e) {
            throw new IllegalArgumentException("구글 로그인 요청 처리 실패");
        }
    }
}
