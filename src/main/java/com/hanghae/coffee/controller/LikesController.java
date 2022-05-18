package com.hanghae.coffee.controller;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.dto.likes.LikesRequestDto;
import com.hanghae.coffee.dto.likes.LikesSliceResponseDto;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.LikesService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api/likes")
@RequiredArgsConstructor
@Slf4j
public class LikesController {

		private final LikesService likesService;



    // 내가 누른 좋아요 조회
    @ResponseBody
    @GetMapping
    public LikesSliceResponseDto getMylikes(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PageableDefault(size = 4, sort = "id", direction = Direction.DESC) Pageable pageable) throws RestException{

			return likesService.getComment(userDetails.getUser().getId(),pageable);
    }


    // 좋아요 등록 & 삭제
    @ResponseBody
    @PostMapping
    public DefaultResponseDto deleteComment(@RequestBody LikesRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
			return likesService.deleteComment(requestDto, userDetails);
		}


}