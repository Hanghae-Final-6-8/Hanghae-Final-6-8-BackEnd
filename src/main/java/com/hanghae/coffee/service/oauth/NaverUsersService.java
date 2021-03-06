package com.hanghae.coffee.service.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.coffee.dto.oauthProperties.OauthNaverPropertiesDto;
import com.hanghae.coffee.dto.oauthProperties.UserInfoDto;
import com.hanghae.coffee.model.OauthType;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.users.UsersRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

    private final UsersRepository usersRepository;

    @Override
    public String getOauthRedirectURL() {

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("redirect_uri", OauthNaverPropertiesDto.naverRedirectUrl);
        params.put("client_secret", OauthNaverPropertiesDto.naverClientSecret);
        params.put("client_id", OauthNaverPropertiesDto.naverClientId);

        String parameterString = params.entrySet().stream()
            .map(x -> x.getKey() + "=" + x.getValue())
            .collect(Collectors.joining("&"));

        System.out.println(OauthNaverPropertiesDto.naverOauthRequestUrl + "?" + parameterString);

        return OauthNaverPropertiesDto.naverOauthRequestUrl + "?" + parameterString;
    }

    @Transactional
    @Override
    public Users doLogin(String code) throws JsonProcessingException {

        // 1. "?????? ??????"??? "????????? ??????" ??????
        String accessToken = getAccessToken(code);

        // 2. ???????????? ????????? API ??????
        UserInfoDto userInfoDto = getNaverUserInfo(accessToken);

        // 3. ???????????? ????????????
        Users naverUser = registerNaverUserIfNeeded(userInfoDto);

        return naverUser;
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        // HTTP Header ??????
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body ??????
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", OauthNaverPropertiesDto.naverClientId);
        body.add("client_secret", OauthNaverPropertiesDto.naverClientSecret);
        body.add("redirect_uri", OauthNaverPropertiesDto.naverRedirectUrl);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            OauthNaverPropertiesDto.naverTokenUrl,
            entity,
            String.class
        );

        // HTTP ?????? (JSON) -> ????????? ?????? ??????
        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info(String.valueOf(jsonNode));
        return jsonNode.get("access_token").asText();

    }

    private UserInfoDto getNaverUserInfo(String accessToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();

        // HTTP Header ??????
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP ?????? ?????????
        HttpEntity httpEntity = new HttpEntity(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
            OauthNaverPropertiesDto.naverUserInfoUrl,
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
        String profile_image_url =
            jsonNode.get("response").has("profile_image") ?
                jsonNode.get("response").get("profile_image").asText() : null;

        log.info("????????? ????????? ??????: " + id + ", " + nickname + ", " + ", " + profile_image_url);
        return UserInfoFactory.getOAuth2UserInfo(OauthType.NAVER, id, nickname, null,
            profile_image_url);

    }

    private Users registerNaverUserIfNeeded(UserInfoDto userInfoDto) {
        // DB ??? ????????? Naver Id ??? ????????? ??????
        String naverId = userInfoDto.getAuthId();
        Optional<Users> naverUsers = usersRepository.findAllByAuthId(naverId);
        if (naverUsers.isEmpty()) {
            // ????????????
            naverUsers = Optional.of(Users.createUsers(userInfoDto));
            usersRepository.save(naverUsers.get());
        }


        return naverUsers.get();
    }

}
