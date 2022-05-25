package com.hanghae.coffee.dto.comments;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CommentsResponseDto extends DefaultResponseDto {
	private CommentsDto data;
}
