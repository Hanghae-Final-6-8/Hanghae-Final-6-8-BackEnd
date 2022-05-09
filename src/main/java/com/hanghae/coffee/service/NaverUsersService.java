package com.hanghae.coffee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.coffee.dto.oauthProperties.OauthPropertiesDto;
import com.hanghae.coffee.dto.oauthProperties.UserInfoDto;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.UsersRepository;
import com.hanghae.coffee.security.jwt.JwtTokenProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class NaverUsersService implements OauthUsersService {

    private final static String NAVER_OAUTH_REQUEST_URL = "https://nid.naver.com/oauth2.0/authorize";
    private final static String NAVER_TOKEN_BASE_URL = "https://nid.naver.com/oauth2.0/token";
    private final static String NAVER_USER_INFO_URL = "https://openapi.naver.com/v1/nid/me";
    private final static String NAVER_REDIRECT_URL = "http://localhost:8080/api/user/login/naver/callback";


    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OauthPropertiesDto oauthPropertiesDto;

    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("redirect_uri", NAVER_REDIRECT_URL);
        params.put("client_secret", oauthPropertiesDto.getNaver().get("client").getSecret());
        params.put("client_id", oauthPropertiesDto.getNaver().get("client").getId());

        String parameterString = params.entrySet().stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining("&"));

        System.out.println(NAVER_OAUTH_REQUEST_URL + "?" + parameterString);

        return NAVER_OAUTH_REQUEST_URL + "?" + parameterString;
    }

    @Transactional
    @Override
    public String doLogin(String code) throws JsonProcessingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 네이버 API 호출
        UserInfoDto userInfoDto = getNaverUserInfo(accessToken);

        // 3. 필요시에 회원가입
        Users naverUser = registerNaverUserIfNeeded(userInfoDto);

        return String.valueOf(naverUser);
    }

	@Override
	public String doLogout(String accessToken, String refreshToken) throws JsonProcessingException {
		return null;
	}

	@Override
	public String reissue(String refreshToken) throws JsonProcessingException {
		return null;
	}

	private String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        // HTTP Header 생성
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", oauthPropertiesDto.getNaver().get("client").getId());
        body.add("client_secret", oauthPropertiesDto.getNaver().get("client").getSecret());
        body.add("redirect_uri", NAVER_REDIRECT_URL);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            NAVER_TOKEN_BASE_URL,
            entity,
            String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info(String.valueOf(jsonNode));
        return jsonNode.get("access_token").asText();

    }

    private UserInfoDto getNaverUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();

        // HTTP Header 생성
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity httpEntity = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            NAVER_USER_INFO_URL,
            httpEntity,
            String.class
        );

        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info(String.valueOf(jsonNode));
        String id = jsonNode.get("response").get("id").asText();
        String nickname = jsonNode.get("response")
            .get("nickname").asText();
        String email = jsonNode.get("response")
            .get("email").asText();
        String profile_image_url =
            jsonNode.get("response").has("profile_image") ?
                jsonNode.get("response").get("profile_image").asText() : null;

        log.info("네이버 사용자 정보: " + id + ", " + nickname + ", " + email + ", " + profile_image_url);
        return UserInfoFactory.getOAuth2UserInfo(OauthType.NAVER, id, nickname, email,
            profile_image_url);

    }

    private Users registerNaverUserIfNeeded(UserInfoDto userInfoDto) {
        // DB 에 중복된 Naver Id 가 있는지 확인
        String naverId = userInfoDto.getAuthId();
        Users naverUsers = usersRepository.findAllByAuthId(naverId)
            .orElse(null);
        if (naverUsers == null) {
            // 회원가입
            String accessToken = jwtTokenProvider.createAccessToken(naverId);
            String refreshToken = jwtTokenProvider.createRefreshToken(naverId);
            naverUsers = Users.createUsers(userInfoDto, accessToken,refreshToken);
            usersRepository.save(naverUsers);
        } else {
            naverUsers = Users.updateUsers(naverUsers, userInfoDto);
//            usersRepository.save(naverUsers);     //dirty checking 으로 생략가능

        }

        return naverUsers;
    }

}
