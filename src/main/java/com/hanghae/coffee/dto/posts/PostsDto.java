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
	private String posts_image;
	private String profile_url;

	private String tag_name;
	private LocalDateTime created_at;
	private LocalDateTime modified_at;
	private Long likes_count;
	private Long isLikes;
}
