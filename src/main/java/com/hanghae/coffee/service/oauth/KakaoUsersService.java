package com.hanghae.coffee.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.coffee.dto.oauthProperties.OauthKakaoPropertiesDto;
import com.hanghae.coffee.dto.oauthProperties.UserInfoDto;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.service.users.users.UsersRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoUsersService implements OauthUsersService {

    private final UsersRepository usersRepository;

    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("redirect_uri", OauthKakaoPropertiesDto.kakaoRedirectUrl);
        params.put("client_id", OauthKakaoPropertiesDto.kakaoClientId);

        String parameterString = params.entrySet().stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining("&"));

        System.out.println(OauthKakaoPropertiesDto.kakaoOauthRequestUrl + "?" + parameterString);

        return OauthKakaoPropertiesDto.kakaoOauthRequestUrl + "?" + parameterString;
    }

    @Transactional
    @Override
    public Users doLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        UserInfoDto userInfoDto = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        Users kakaoUser = registerKakaoUserIfNeeded(userInfoDto);

        return kakaoUser;
    }

    private String getAccessToken(String code) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();

        // HTTP Header 생성
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", OauthKakaoPropertiesDto.kakaoClientId);
        body.add("redirect_uri", OauthKakaoPropertiesDto.kakaoRedirectUrl);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
            new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            OauthKakaoPropertiesDto.kakaoTokenUrl,
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private UserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();

        // HTTP Header 생성
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            OauthKakaoPropertiesDto.kakaoUserInfoUrl,
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info(String.valueOf(jsonNode));

        String id = jsonNode.get("id").asText();
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        // 필수 값이 아니라 값이 없으면 null로 초기화
        String email =
            jsonNode.get("kakao_account").has("email") ?
                jsonNode.get("kakao_account").get("email").asText() : null;

        String profile_image_url =
            jsonNode.get("kakao_account").get("profile").has("profile_image_url") ?
                jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText()
                : null;

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email + ", " + profile_image_url);
        return UserInfoFactory.getOAuth2UserInfo(OauthType.KAKAO, id, nickname, email, profile_image_url);

    }

    private Users registerKakaoUserIfNeeded(UserInfoDto userInfoDto) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        String kakaoId = userInfoDto.getAuthId();
        Users kakaoUsers = usersRepository.findAllByAuthId(kakaoId)
            .orElse(null);
        if (kakaoUsers == null) {
            // 회원가입

            kakaoUsers = Users.createUsers(userInfoDto);
            usersRepository.save(kakaoUsers);
        } else {

            kakaoUsers = Users.updateUsers(kakaoUsers, userInfoDto);

        }

        return kakaoUsers;
    }

}
