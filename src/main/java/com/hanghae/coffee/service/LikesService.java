package com.hanghae.coffee.service;
import com.hanghae.coffee.dto.likes.LikesInterfaceJoinVO;
import com.hanghae.coffee.dto.likes.LikesRequestDto;
import com.hanghae.coffee.repository.LikesRepository;
import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.dto.likes.LikesSliceResponseDto;
import com.hanghae.coffee.model.Likes;
import com.hanghae.coffee.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class LikesService {

	private final LikesRepository likesRepository;

	public LikesSliceResponseDto getComment(Long id, Pageable pageable) {

		Slice<LikesInterfaceJoinVO> likesInterfaceJoinVOSlice = likesRepository.findAllByOrderByUser_IdwithPosts(id, pageable);

		return LikesSliceResponseDto
				.builder()
				.status(HttpStatus.OK)
				.msg("success")
				.data(likesInterfaceJoinVOSlice)
				.build();
	}

	public DefaultResponseDto deleteComment(LikesRequestDto requestDto,
			UserDetailsImpl userDetails) {
		Long posts_id = requestDto.getPosts().getId();

		Likes likes = likesRepository.findByPosts_Id(posts_id);

		if(likes == null){
			// 등록
			likes = new Likes(requestDto.getPosts(),userDetails.getUser());
			likesRepository.save(likes);

			return DefaultResponseDto
					.builder()
					.status(HttpStatus.OK)
					.msg("success")
					.build();
		} else{
			if(likes.getUsers().getId().equals(userDetails.getUser().getId())){
				// 삭제
				likesRepository.deleteById(likes.getId());
				return DefaultResponseDto
						.builder()
						.status(HttpStatus.OK)
						.msg("delete")
						.build();
			} else {
				return DefaultResponseDto
						.builder()
						.status(HttpStatus.FORBIDDEN)
						.msg("forbidden")
						.build();
			}
		}
	}

}
