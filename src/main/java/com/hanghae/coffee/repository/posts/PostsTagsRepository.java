package com.hanghae.coffee.repository.posts;

import com.hanghae.coffee.model.PostsImage;
import com.hanghae.coffee.model.PostsTags;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsTagsRepository extends JpaRepository<PostsTags,Long> {
	PostsTags deleteByPosts_Id(Long postsId);

	Optional<PostsTags> findByPosts_Id(Long id);
}
