package com.hanghae.coffee.dto.comments;

import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.Users;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentsDto {

	private Long id;
	private String content;
	private Users users;
	private Posts posts;
	private LocalDateTime ModifiedAt;
	private LocalDateTime CreatedAt;
}
