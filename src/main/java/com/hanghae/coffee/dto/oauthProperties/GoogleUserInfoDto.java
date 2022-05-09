package com.hanghae.coffee.dto.oauthProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GoogleUserInfoDto {
    private String id;
    private String name;
    private String email;
}
