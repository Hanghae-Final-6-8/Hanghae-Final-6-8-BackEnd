package com.hanghae.coffee.dto.comments;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentsDto {

	private Long comments_id;
	private String content;
	private String nickname;
	private Long posts_id;
	private LocalDateTime modified_at;
	private LocalDateTime created_at;



}
