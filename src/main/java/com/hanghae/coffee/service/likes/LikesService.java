package com.hanghae.coffee.service.likes;

import com.hanghae.coffee.advice.ErrorCode;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.likes.LikesRequestDto;
import com.hanghae.coffee.dto.posts.PostsDto;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.repository.likes.LikesRepository;
import com.hanghae.coffee.model.Likes;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LikesService {

	private final LikesRepository likesRepository;
	private final PostsRepository postsRepository;

	public Slice<PostsDto> getComment(Long id, Pageable pageable) {

		return likesRepository.getAllByOrderByUser_IdwithPosts(id, pageable);
	}

	public String deleteComment(LikesRequestDto requestDto,
			UserDetailsImpl userDetails) {
		Long posts_id = requestDto.getPosts_id();


		Posts posts = postsRepository.findById(posts_id).orElseThrow(
				() -> new RestException(ErrorCode.NOT_FOUND_POST)
		);
		Optional<Likes> likes =likesRepository.findByPosts_IdAndUsers_Id(posts.getId(),userDetails.getUser().getId());

		if(likes.isEmpty()){
			// 등록
			Likes newLikes = new Likes(posts,userDetails.getUser());
			likesRepository.save(newLikes);

			return "success";
		} else{
			if(likes.get().getUsers().getId().equals(userDetails.getUser().getId())){
				// 삭제
				likesRepository.deleteById(likes.get().getId());
				return "delete";
			} throw new RestException(ErrorCode.PERMISSION_DENIED_TO_DELETE);
			}
		}
	}

