package com.hanghae.coffee.dto.comments;

import com.hanghae.coffee.model.Posts;
import lombok.Getter;

@Getter
public class CommentsRequestDto {
	private Posts posts;
	private Long comments_id;
	private String content;

}
