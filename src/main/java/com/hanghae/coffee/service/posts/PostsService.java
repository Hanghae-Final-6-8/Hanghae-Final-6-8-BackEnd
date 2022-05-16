package com.hanghae.coffee.service.posts;


import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.PostsInterfaceJoinVO;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.repository.posts.PostsTagsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
//    private final LikeRepository likeRepository;



    public PostsInterfaceJoinVO getPost(Long post_id) {

//        boolean Like = likeRepository.findByUseridAndPostid(user_id,post_id);

//        return new PostsRequestDto(board,Like);
        return postsRepository.findPostsByIdWithPostImages(post_id);
    }

    public Posts writePost(String title, String content, UserDetailsImpl userDetails) throws IOException {
        log.info("writePost");
        Posts posts = new Posts(title,content,userDetails.getUser());

        return postsRepository.save(posts);

    }

    @Transactional
    public Posts updatePost(Long post_id,String title, String content, UserDetailsImpl userDetails) {
        Posts posts = postsRepository.findById(post_id).orElseThrow(
                () -> new RestException(HttpStatus.BAD_REQUEST, "해당하는 게시물이 존재하지 않습니다.")
        );
        posts.update(title,content,userDetails.getUser());

        return posts;
    }







//    public PostsRequestDto getMyPost(Long user_id) {
//        Posts posts = (Posts) postsRepository.findAllByOrderByUser_id(user_id);
//        return posts;
    }

