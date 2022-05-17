package com.hanghae.coffee.dto.beans;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class BeansListResponseDto extends DefaultResponseDto {

    private List<BeansListDto> data;

}
