package com.hanghae.coffee.dto.likes;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class LikesResponseDto extends DefaultResponseDto {
	private LikesInterfaceJoinVO data;
}
