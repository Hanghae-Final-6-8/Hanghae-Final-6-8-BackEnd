package com.hanghae.coffee.dto.taste;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class TasteResponseDto extends DefaultResponseDto {

    private TasteDto data;

}
