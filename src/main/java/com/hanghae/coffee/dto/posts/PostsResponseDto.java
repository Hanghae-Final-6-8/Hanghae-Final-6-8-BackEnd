package com.hanghae.coffee.dto.posts;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PostsResponseDto extends DefaultResponseDto {
	private PostsInterfaceJoinVO data;
}
