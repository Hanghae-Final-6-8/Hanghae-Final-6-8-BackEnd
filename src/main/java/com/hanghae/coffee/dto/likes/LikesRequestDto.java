package com.hanghae.coffee.dto.likes;

import com.hanghae.coffee.model.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikesRequestDto  {
	private Posts posts;
}
