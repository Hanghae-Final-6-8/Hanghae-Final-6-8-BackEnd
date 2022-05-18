package com.hanghae.coffee.repository;

import com.hanghae.coffee.dto.comments.CommentsInterfaceJoinVO;
import com.hanghae.coffee.model.Comments;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments,Long> {

	Slice<CommentsInterfaceJoinVO> findAllByPosts_Id(Long posts_id,Pageable pageable);


	Slice<CommentsInterfaceJoinVO> findAllByUsers_Id(Long id, Pageable pageable);

	CommentsInterfaceJoinVO findByPosts_Id(Long post_id);
}
