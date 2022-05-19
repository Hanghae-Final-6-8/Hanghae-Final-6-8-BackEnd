package com.hanghae.coffee.dto.comments;

import com.hanghae.coffee.model.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentsRequestDto {
	private Posts posts;
	private Long comments_id;
	private String content;

}
