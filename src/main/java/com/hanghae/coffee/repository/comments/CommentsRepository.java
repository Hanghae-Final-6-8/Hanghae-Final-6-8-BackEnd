package com.hanghae.coffee.repository.comments;

import com.hanghae.coffee.model.Comments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments,Long>,CommentsRepositoryCustom {

//	Slice<Comments> findAllByPosts_Id(Long posts_id,Pageable pageable);
//
//
//	Slice<Comments> findAllByUsers_Id(Long id, Pageable pageable);


}
