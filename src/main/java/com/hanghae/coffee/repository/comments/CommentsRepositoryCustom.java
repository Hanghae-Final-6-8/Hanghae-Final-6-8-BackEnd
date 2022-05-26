package com.hanghae.coffee.repository.comments;

import com.hanghae.coffee.dto.comments.CommentsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommentsRepositoryCustom {

    Slice<CommentsDto> getAllByPosts_Id(Long posts_id, Pageable pageable);
    Slice<CommentsDto> getAllByUsers_Id(Long id, Pageable pageable);
    CommentsDto getByIdWithDto(Long id);

}
