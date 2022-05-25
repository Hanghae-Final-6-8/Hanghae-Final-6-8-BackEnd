package com.hanghae.coffee.dto.likes;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Slice;

@Getter
@Setter
@SuperBuilder
public class LikesSliceResponseDto extends DefaultResponseDto {
	private Slice<LikesDto> data;
}
