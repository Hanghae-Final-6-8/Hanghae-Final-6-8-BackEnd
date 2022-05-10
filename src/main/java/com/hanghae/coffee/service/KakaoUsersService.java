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

    private final static String KAKAO_OAUTH_REQUEST_URL = "https://kauth.kakao.com/oauth/authorize";
    private final static String KAKAO_TOKEN_BASE_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_TOKEN_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final OauthPropertiesDto oauthPropertiesDto;

    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("redirect_uri", "http://localhost:8080/api/user/login/kakao/callback");
        params.put("client_id", oauthPropertiesDto.getKakao().get("client").getId());

        String parameterString = params.entrySet().stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining("&"));

        System.out.println(KAKAO_OAUTH_REQUEST_URL + "?" + parameterString);

        return KAKAO_OAUTH_REQUEST_URL + "?" + parameterString;
    }

    @Transactional
    @Override
    public String doLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 카카오 API 호출
        UserInfoDto userInfoDto = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        Users kakaoUser = registerKakaoUserIfNeeded(userInfoDto);

        // 4. 강제 로그인 처리
//        forceLogin(kakaoUser);
        return String.valueOf(kakaoUser);
    }

    @Override
    public String doLogout(String accessToken, String refreshToken){
        return null;
    }

    @Override
    public String reissue(String refreshToken){
        return null;
    }

    private String getAccessToken(String code) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();

        // HTTP Header 생성
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", oauthPropertiesDto.getKakao().get("client").getId());
        body.add("redirect_uri", "http://localhost:8080/api/user/login/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
            new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            KAKAO_TOKEN_BASE_URL,
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
            KAKAO_TOKEN_INFO_URL,
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
            String requestToken = jwtTokenProvider.createRefreshToken(kakaoId);
            kakaoUsers = Users.createUsers(userInfoDto,requestToken);
            usersRepository.save(kakaoUsers);
        } else {

            kakaoUsers = Users.updateUsers(kakaoUsers, userInfoDto);
//            usersRepository.save(kakaoUsers);     //dirty checking 으로 생략가능..?

        }

        return kakaoUsers;
    }

//    private void forceLogin(Users kakaoUser) {
//        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }

}
