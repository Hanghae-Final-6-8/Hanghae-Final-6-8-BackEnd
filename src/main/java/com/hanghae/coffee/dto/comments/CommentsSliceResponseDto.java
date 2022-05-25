package com.hanghae.coffee.dto.comments;

import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.model.Comments;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Slice;

@Getter
@Setter
@SuperBuilder
public class CommentsSliceResponseDto extends DefaultResponseDto {
	private Slice<Comments> data;
}
