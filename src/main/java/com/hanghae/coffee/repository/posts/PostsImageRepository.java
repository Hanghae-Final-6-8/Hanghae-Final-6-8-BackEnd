package com.hanghae.coffee.repository.posts;

import com.hanghae.coffee.model.PostsImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsImageRepository extends JpaRepository<PostsImage, Long> {
    Optional<PostsImage> findByPosts_Id(Long id);
}
