package com.hanghae.coffee.service.posts;

import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.PostsImage;
import com.hanghae.coffee.repository.posts.PostsImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsImageService {

    private final PostsImageRepository postsImageRepository;

    @Transactional(readOnly = false)
    public void imageSave(Posts posts, String url){

        PostsImage postsImage = new PostsImage(posts,url);

        postsImageRepository.save(postsImage);
    }

    public String getImageUrl(Long postId){

        PostsImage postsImage = postsImageRepository.findByPosts_Id(postId).orElse(null);

        return postsImage != null ? postsImage.getImageUrl() : null;

    }

    @Transactional(readOnly = false)
    public void imageDelete(Long postId){
        postsImageRepository.deleteByPosts_Id(postId);
    }


}
