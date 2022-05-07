package com.hanghae.coffee.dto.oauthProperties;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("oauth")
public class OauthPropertiesDto {


    private Map<String, Client> kakao;

    // getters and setters

    public Map<String, Client> getKakao() {
        return kakao;
    }

    public void setKakao(
        Map<String, Client> kakao) {
        this.kakao = kakao;
    }

    public static class Client {

        private String id;
        private String secret;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

    }

}
