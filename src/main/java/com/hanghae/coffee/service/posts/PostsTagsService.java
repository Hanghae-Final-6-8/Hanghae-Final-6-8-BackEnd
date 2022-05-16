package com.hanghae.coffee.service.posts;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.PostsTags;
import com.hanghae.coffee.model.Tags;
import com.hanghae.coffee.repository.posts.PostsTagsRepository;
import com.hanghae.coffee.repository.posts.TagsRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostsTagsService {

    private final PostsTagsRepository postsTagsRepository;
    private final TagsRepository tagsRepository;


    // 게시물 태그 업데이트
    @Transactional
    public void updatePostsTags(Posts posts, String tagName){
        Optional<PostsTags> postsTags = postsTagsRepository.findByPosts_Id(posts.getId());
        Tags newTagname = tagsRepository.findById(postsTags.get().getTags().getId()).orElseThrow(
            () -> new RestException(HttpStatus.BAD_REQUEST, "해당 태그가 없습니다.")
        );

        newTagname.updateTags(tagName);
    }

    // 게시물 태그 저장
    public void putPostsTags(Posts posts, String tagName){
        Tags tags = tagsRepository.save(new Tags(tagName));
        PostsTags postsTags = new PostsTags(posts,tags);
        postsTagsRepository.save(postsTags);
    }



}
