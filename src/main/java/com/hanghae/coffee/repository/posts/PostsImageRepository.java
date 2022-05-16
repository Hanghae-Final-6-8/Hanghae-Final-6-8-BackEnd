package com.hanghae.coffee.repository.posts;

import com.hanghae.coffee.model.PostsImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsImageRepository extends JpaRepository<PostsImage, Long> {
    PostsImage findByPosts_Id(Long id);
}
