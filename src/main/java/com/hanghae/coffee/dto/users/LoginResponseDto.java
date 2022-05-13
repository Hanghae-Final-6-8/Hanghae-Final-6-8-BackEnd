package com.hanghae.coffee.dto.users;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class LoginResponseDto extends DefaultResponseDto {

    private HashMap<String,String> data;

}
