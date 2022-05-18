package com.hanghae.coffee.dto.likes;

import com.hanghae.coffee.model.Users;
import java.time.LocalDateTime;

public interface LikesInterfaceJoinVO {
	 Long getComments_id();
	 String getContent();
	 Users getUsers();
	 LocalDateTime getCreated_at();
	 LocalDateTime getModified_at();

	LikesInterfaceJoinVO orElseThrow(Object 해당_내용이_없습니다);
}
