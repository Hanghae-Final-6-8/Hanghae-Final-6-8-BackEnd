package com.hanghae.coffee.repository;

import com.hanghae.coffee.dto.likes.LikesInterfaceJoinVO;
import com.hanghae.coffee.model.Likes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes,Long> {


	@Query("SELECT p.id as posts_id, "
			+ "p.title as title, "
			+ "p.content as content,"
			+ "p.createdAt as created_at, "
			+ "p.modifiedAt as modified_at, "
			+ "p.users.nickname as nickname, "
			+ "pi.imageUrl as posts_image, "
			+ "pt.tags.tagName as tag_name "
			+ "FROM Likes l "
			+ "JOIN fetch Posts p ON p.id = l.posts.id "
			+ "JOIN fetch Users u ON u.id = l.users.id "
			+ "JOIN fetch PostsImage pi ON p.id = pi.posts.id "
			+ "JOIN fetch PostsTags pt ON p.id = pt.posts.id "
			+ "JOIN fetch Tags t ON pt.tags.id = t.id "
			+ "where p.users.id = :id ")
	Slice<LikesInterfaceJoinVO> findAllByOrderByUser_IdwithPosts(Long id, Pageable pageable);




	Likes findByPosts_Id(Long posts_id);
}
