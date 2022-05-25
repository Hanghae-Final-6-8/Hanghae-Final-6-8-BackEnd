package com.hanghae.coffee.controller;


import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.dto.posts.PostsRequestDto;
import com.hanghae.coffee.dto.posts.PostsResponseDto;
import com.hanghae.coffee.dto.posts.PostsSliceResponseDto;
import com.hanghae.coffee.model.Posts;

import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.posts.FileService;
import com.hanghae.coffee.service.posts.PostsImageService;
import com.hanghae.coffee.service.posts.PostsService;
import com.hanghae.coffee.service.posts.PostsTagsService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final static String DIRECTORY_URL = "posts/images";

    private final PostsService postsService;
    private final PostsImageService postsImageService;
    private final FileService fileService;
    private final PostsTagsService postsTagsService;

    /**
     * 게시물 조회 요청 처리
     *
     * @param "page 무한 스크롤을 위한 페이지 번호"
     * @return 게시물 전체 조회 결과를 Slice 형식(size 만큼)으로 반환
     */
    @ResponseBody
    @GetMapping("posts")
    public PostsSliceResponseDto getPost(Pageable pageable,
        @AuthenticationPrincipal UserDetailsImpl userDetails){

        return postsService.getPostList(userDetails, pageable);
    }

    // 게시글 세부 조회
    @ResponseBody
    @GetMapping("posts/{post_id}")
    public PostsResponseDto getDetailPost(@PathVariable Long post_id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws RestException{

        return postsService.getDetailPost(post_id,userDetails);
    }

    // 내 게시글 전체 조회
    @ResponseBody
    @GetMapping("posts/mine")
    public PostsSliceResponseDto getMyPost(
        @AuthenticationPrincipal UserDetailsImpl userDetails, Pageable pageable) throws RestException{
        Long user_id;
        if(userDetails == null){
            user_id = 0L;
        } else{
            user_id = userDetails.getUser().getId();
        }
        return postsService.getMyPostList(userDetails, pageable);
    }

    //게시글 추가
    @ResponseBody
    @PostMapping(value = "posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PostsResponseDto writePost(
        @RequestPart(value = "title") String title,
        @RequestPart(value = "content", required = false) String content,
        @RequestPart(value = "tag_name", required = false) String tagName,
        @RequestPart(value = "posts_image", required = false) MultipartFile posts_image,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        Posts posts = postsService.writePosts(title, content, tagName, posts_image, userDetails);

        return postsService.getDetailPost(posts.getId(),userDetails);
    }


    //게시글 수정
    @ResponseBody
    @PostMapping("posts/update")
    public PostsResponseDto updatePost( Long posts_id,
        @RequestPart(value = "title") String title,
        @RequestPart(value = "content", required = false) String content,
        @RequestPart(value = "tag_name", required = false) String tagName,
        @RequestPart(value = "posts_image", required = false) MultipartFile picture,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        Posts posts = postsService.updatePosts(posts_id, title, content, tagName, picture,
            userDetails);

        return postsService.getDetailPost(posts.getId(),userDetails);
    }




    // 게시글 삭제
    // 유저 정보 확인 후 권한 확인
    @ResponseBody
    @PostMapping("posts/delete")
    public DefaultResponseDto deletePost(@RequestBody PostsRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postsService.deletePost(requestDto, userDetails);

        return DefaultResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .build();
    }




}