package com.hanghae.coffee.controller;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.model.Comments;
import com.hanghae.coffee.dto.comments.CommentsRequestDto;
import com.hanghae.coffee.dto.comments.CommentsSliceResponseDto;
import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.comments.CommentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    public CommentsSliceResponseDto getComment(@PathVariable Long posts_id,Pageable pageable){
        return commentsService.getComment(posts_id,pageable);
    }


    // 내 댓글 전체 조회
    @ResponseBody
    @GetMapping("comments/mine")
    public CommentsSliceResponseDto getMyComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,Pageable pageable) throws RestException{
        return commentsService.getMyComment(userDetails.getUser().getId(), pageable);
    }

    //댓글 추가
    @ResponseBody
    @PostMapping(value = "comments")
    public Comments writeComment(@RequestBody CommentsRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws RestException {
        log.info("writePost");
        return commentsService.writeComment(requestDto,userDetails);

    }

    // 댓글 삭제
    // 유저 정보 확인 후 권한 확인
    // @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PostMapping("comments/delete")
    public DefaultResponseDto deleteComment(@RequestBody CommentsRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentsService.deleteComment(requestDto, userDetails);
    }


}