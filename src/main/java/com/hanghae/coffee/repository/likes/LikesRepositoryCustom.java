package com.hanghae.coffee.repository.likes;

import com.hanghae.coffee.dto.likes.LikesDto;

import com.hanghae.coffee.dto.posts.PostsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LikesRepositoryCustom {

    Slice<PostsDto> getAllByOrderByUser_IdwithPosts(Long id, Pageable pageable);

}
