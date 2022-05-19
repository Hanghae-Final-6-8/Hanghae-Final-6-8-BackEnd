package com.hanghae.coffee.dto.users;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CountInfoByUserDto {

    private Long favorites_count;

    private Long posts_count;

    private Long likes_count;

}
