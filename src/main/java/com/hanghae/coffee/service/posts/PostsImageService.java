package com.hanghae.coffee.service.posts;

import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.PostsImage;
import com.hanghae.coffee.repository.posts.PostsImageRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostsImageService {

    private final PostsImageRepository postsImageRepository;

    @Transactional
    public void imageSave(Posts posts, String url){

        PostsImage postsImage = new PostsImage(posts,url);

        postsImageRepository.save(postsImage);
    }

    public Optional<String> getImageUrl(Long postId){

        Optional<PostsImage> postsImage = postsImageRepository.findByPosts_Id(postId);

        return postsImage.map(PostsImage::getImageUrl);

    }

    @Transactional
    public void imageDelete(Long postId){
        postsImageRepository.deleteByPosts_Id(postId);
    }


}
