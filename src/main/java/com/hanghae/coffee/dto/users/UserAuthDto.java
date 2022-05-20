package com.hanghae.coffee.dto.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAuthDto {

    private String nickname;

    private String profile_url;

    private Long tasteId;

}
