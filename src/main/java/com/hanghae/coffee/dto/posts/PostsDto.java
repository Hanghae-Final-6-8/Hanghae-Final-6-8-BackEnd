package com.hanghae.coffee.dto.posts;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsDto {
	private Long posts_id;
	private String title;
	private String content;
	private String nickname;
	private String imageUrl;

	private String tagName;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private Long likes_Count;
	private Long isLikes;
}
