package com.hanghae.coffee.dto.cafe;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CafeResponseDto extends DefaultResponseDto {

    private List<CafeDto> data;

}
