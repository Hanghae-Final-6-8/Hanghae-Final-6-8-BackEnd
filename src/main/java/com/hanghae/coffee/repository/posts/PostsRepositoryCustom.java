package com.hanghae.coffee.repository.posts;
import com.hanghae.coffee.dto.posts.PostsDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostsRepositoryCustom {

    Slice<PostsDto> getPostsAllByUsers_Id(Long id, Pageable pageable);
    Slice<PostsDto> getAllWithPostImages(Long id, Pageable pageable);
    PostsDto getPostsByIdWithPostImages(Long id, Long user_id);

}
