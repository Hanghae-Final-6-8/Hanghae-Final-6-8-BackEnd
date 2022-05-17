package com.hanghae.coffee.dto.beans;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class BeansResponseDto extends DefaultResponseDto {

    private BeansDto data;

}
