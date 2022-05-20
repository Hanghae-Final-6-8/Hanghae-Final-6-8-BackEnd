package com.hanghae.coffee.dto.posts;

import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.PostsImage;
import com.hanghae.coffee.model.PostsTags;
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
	 Long getLikes_count();
	 Long getIsLikes();




	PostsInterfaceJoinVO orElseThrow(Object 해당_내용이_없습니다);
}
