package com.hanghae.coffee.repository.likes;

import com.hanghae.coffee.model.Likes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes,Long>, LikesRepositoryCustom {

	Optional<Likes> findByPosts_IdAndUsers_Id(Long id, Long users_id);


//	@Query("SELECT p.id as posts_id, "
//			+ "p.title as title, "
//			+ "p.content as content,"
//			+ "p.createdAt as created_at, "
//			+ "p.modifiedAt as modified_at, "
//			+ "p.users.nickname as nickname, "
//			+ "pi.imageUrl as posts_image, "
//			+ "pt.tags.tagName as tag_name "
//			+ "FROM Posts p "
//			+ "Left join fetch Likes l on p.id = l.posts.id "
//			+ "Left JOIN fetch PostsImage pi ON p.id = pi.posts.id "
//			+ "Left JOIN fetch PostsTags pt ON p.id = pt.posts.id "
//			+ "Left JOIN fetch Tags t ON pt.tags.id = t.id "
//			+ "where l.users.id = :id")
//	Slice<PostsDto> findAllByOrderByUser_IdwithPosts(Long id, Pageable pageable);



	Likes findByPosts_Id(Long posts_id);
}
