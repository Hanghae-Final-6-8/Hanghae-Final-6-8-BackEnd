package com.hanghae.coffee.dto.users;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CountInfoByUserResponseDto extends DefaultResponseDto {

    private CountInfoByUserDto data;

}