package com.hanghae.coffee.service.posts;

import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.PostsTags;
import com.hanghae.coffee.model.Tags;
import com.hanghae.coffee.repository.posts.PostsTagsRepository;
import com.hanghae.coffee.repository.posts.TagsRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostsTagsService {

    private final PostsTagsRepository postsTagsRepository;
    private final TagsRepository tagsRepository;


    // 게시물 태그 업데이트
    @Transactional
    public void updatePostsTags(Posts posts, List<String> tagName){
        postsTagsRepository.deleteAllByPosts_Id(posts.getId());
        for(String name:tagName){
            Tags tags = tagsRepository.findByTagName(name);
            if(tags == null){
                tags = new Tags(name);
                tagsRepository.save(tags);
            }

            PostsTags newpostsTags = new PostsTags(posts,tags);
            postsTagsRepository.save(newpostsTags);
        }
    }

    // 게시물 태그 저장
    public void putPostsTags(Posts posts, List<String> tagName){
        tagName = tagName.stream().distinct().collect(Collectors.toList());
        for(String name:tagName){
            Tags tags = tagsRepository.findByTagName(name);
            if(tags == null){
                tags = new Tags(name);
                tagsRepository.save(tags);
            }
            PostsTags postsTags = new PostsTags(posts,tags);
            postsTagsRepository.save(postsTags);
        }
    }

    public void deletePostsTags(Posts posts){
        Optional<PostsTags> postsTagsOptional = postsTagsRepository.findByPosts_Id(posts.getId());
        postsTagsOptional.ifPresent(
            postsTags -> postsTagsRepository.deleteAllByPosts_Id(posts.getId())
        );
    }



}
