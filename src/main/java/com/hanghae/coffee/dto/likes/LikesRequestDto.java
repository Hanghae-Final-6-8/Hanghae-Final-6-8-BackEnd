package com.hanghae.coffee.dto.likes;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.model.Posts;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class LikesRequestDto extends DefaultResponseDto {
	private Posts posts;
}
