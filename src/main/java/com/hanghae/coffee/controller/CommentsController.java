package com.hanghae.coffee.controller;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.repository.CommentsRepository;
import com.hanghae.coffee.dto.comments.CommentsRequestDto;
import com.hanghae.coffee.dto.comments.CommentsSliceResponseDto;
import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.model.Comments;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.CommentsService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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

    private final CommentsRepository commentsRepository;
    private final CommentsService commentsService;


    // 댓글 목록 조회
    @ResponseBody
    @GetMapping("comments/{posts_id}")
    public CommentsSliceResponseDto getComment(@PathVariable Long posts_id,
        @PageableDefault(size = 4, sort = "id", direction = Direction.ASC) Pageable pageable){
        return commentsService.getComment(posts_id,pageable);
//        return null;
    }


    // 내 댓글 전체 조회
    @ResponseBody
    @GetMapping("comments/mine")
    public CommentsSliceResponseDto getMyComment(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PageableDefault(size = 4, sort = "id", direction = Direction.ASC) Pageable pageable) throws RestException{
        return commentsService.getMyComment(userDetails.getUser().getId(), pageable);
    }

    //댓글 추가
    @ResponseBody
    @PostMapping(value = "comments")
    public DefaultResponseDto writeComment(@RequestBody CommentsRequestDto requestDto,
//        @RequestPart(value = "post_id") Long posts_id,
//        @RequestPart(value = "content") String content,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        log.info("writePost");
        Comments comments = commentsService.writeComment(requestDto,userDetails);

        return DefaultResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .build();
    }

    // 댓글 삭제
    // 유저 정보 확인 후 권한 확인
    @ResponseBody
    @PostMapping("comments/delete")
    public DefaultResponseDto deleteComment(@RequestBody Map<String, Long> param,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long comments_id = param.get("comments_id");

        Comments comments = commentsRepository.findById(comments_id).orElseThrow(
            () -> new NullPointerException("fail")
        );
        Long id = comments.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())){
            commentsRepository.deleteById(comments_id);
        } else{
            return DefaultResponseDto
                .builder()
                .status(HttpStatus.FORBIDDEN)
                .msg("success")
                .build();
        }
        return DefaultResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .build();
    }
}