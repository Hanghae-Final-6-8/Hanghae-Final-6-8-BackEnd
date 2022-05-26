package com.hanghae.coffee.controller;
import com.hanghae.coffee.dto.global.ResponseFormat;
import com.hanghae.coffee.dto.comments.CommentsRequestDto;

import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.comments.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class CommentsController {

    private final CommentsService commentsService;


    // 댓글 목록 조회
    @ResponseBody
    @GetMapping("comments/{posts_id}")
    public ResponseEntity<?> getComment(@PathVariable Long posts_id,Pageable pageable){
        ResponseFormat responseFormat = new ResponseFormat().of(
            commentsService.getComment(posts_id,pageable), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);
    }


    // 내 댓글 전체 조회
    @ResponseBody
    @GetMapping("comments/mine")
    public ResponseEntity<?> getMyComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,Pageable pageable){

        ResponseFormat responseFormat = new ResponseFormat().of(
            commentsService.getMyComment(userDetails.getUser().getId(), pageable), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);
    }

    //댓글 추가
    @ResponseBody
    @PostMapping(value = "comments")
    public ResponseEntity<?> writeComment(@RequestBody CommentsRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("writePost");
        ResponseFormat responseFormat = new ResponseFormat().of(
            commentsService.writeComment(requestDto,userDetails), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    // 댓글 삭제
    // 유저 정보 확인 후 권한 확인
    // @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PostMapping("comments/delete")
    public ResponseEntity<?> deleteComment(@RequestBody CommentsRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseFormat responseFormat = new ResponseFormat().of(
            commentsService.deleteComment(requestDto, userDetails));
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);
    }


}