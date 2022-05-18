package com.hanghae.coffee.dto.posts;

import java.time.LocalDateTime;

public interface PostsInterfaceJoinVO {
	 Long getPosts_id();
	 String getTitle();
	 String getContent();
	 String getNickname();
	 String getPosts_image();
	 String getTag_name();
	 LocalDateTime getCreated_at();
	 LocalDateTime getModified_at();

	PostsInterfaceJoinVO orElseThrow(Object 해당_내용이_없습니다);
}
