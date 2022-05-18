package com.hanghae.coffee.dto.comments;

import com.hanghae.coffee.model.Users;
import java.time.LocalDateTime;

public interface CommentsInterfaceJoinVO {
	 Long getId();
	 String getContent();
	 Users getUsers();
	 LocalDateTime getcreatedAt();
	 LocalDateTime getModifiedAt();

	CommentsInterfaceJoinVO orElseThrow(Object 해당_내용이_없습니다);
}
