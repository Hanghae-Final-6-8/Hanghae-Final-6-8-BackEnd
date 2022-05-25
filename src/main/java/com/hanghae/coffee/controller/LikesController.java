package com.hanghae.coffee.controller;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.global.ResponseFormat;
import com.hanghae.coffee.dto.likes.LikesRequestDto;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.likes.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getMylikes(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
         Pageable pageable) throws RestException{

			ResponseFormat responseFormat = new ResponseFormat().of(
					likesService.getComment(userDetails.getUser().getId(),pageable), "success");
			return new ResponseEntity<>(responseFormat, HttpStatus.OK);
    }


    // 좋아요 등록 & 삭제
    @ResponseBody
    @PostMapping("mine")
    public ResponseEntity<?> deleteComment(@RequestBody LikesRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
			ResponseFormat responseFormat = new ResponseFormat().of(
					likesService.deleteComment(requestDto, userDetails), "success");
			return new ResponseEntity<>(responseFormat, HttpStatus.OK);
		}


}