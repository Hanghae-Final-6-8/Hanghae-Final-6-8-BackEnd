package com.hanghae.coffee.dto.likes;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikesDto {
	private Long comments_id;
	private String content;
	private Long user_id;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
