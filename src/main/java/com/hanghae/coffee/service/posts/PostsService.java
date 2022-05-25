package com.hanghae.coffee.service.posts;


import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.posts.PostsDto;
import com.hanghae.coffee.dto.posts.PostsResponseDto;
import com.hanghae.coffee.dto.posts.PostsSliceResponseDto;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.repository.posts.PostsTagsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.io.IOException;


@RequiredArgsConstructor
@Service
@Slf4j
public class PostsService {

    private final PostsRepository postsRepository;
    private final PostsTagsRepository postsTagsRepository;

    public PostsSliceResponseDto getPostList(Long user_id, Pageable pageable) {


        Slice<PostsDto> postsDtoSlice = postsRepository.getAllWithPostImages(user_id, pageable);


        return PostsSliceResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(postsDtoSlice)
            .build();
    }

    public PostsSliceResponseDto getMyPostList(Long user_id, Pageable pageable) {


        Slice<PostsDto> postsDtoSlice = postsRepository.getPostsAllByUsers_Id(user_id, pageable);

        return PostsSliceResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(postsDtoSlice)
            .build();
    }



    public PostsResponseDto getDetailPost(Long post_id, Long user_id) {
        PostsDto postsDto = postsRepository.getPostsByIdWithPostImages(post_id, user_id);


        return PostsResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(postsDto)
            .build();
    }

    public Posts writePost(String title, String content, UserDetailsImpl userDetails)
        throws IOException {
        log.info("writePost");
        Posts posts = new Posts(title, content, userDetails.getUser());

        return postsRepository.save(posts);

    }

    @Transactional
    public Posts updatePost(Long post_id, String title, String content,
        UserDetailsImpl userDetails) {
        Posts posts = postsRepository.findById(post_id).orElseThrow(
            () -> new RestException(HttpStatus.BAD_REQUEST, "해당하는 게시물이 존재하지 않습니다.")
        );
        posts.update(title, content, userDetails.getUser());

        return posts;
    }

    public Posts getPosts(Long postId, Long userId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(
            () -> new RestException(HttpStatus.BAD_REQUEST, "bad request")
        );

        if (!posts.getUsers().getId().equals(userId)) {

            throw new RestException(HttpStatus.FORBIDDEN, "forbidden");

        }

        return posts;
    }
}

