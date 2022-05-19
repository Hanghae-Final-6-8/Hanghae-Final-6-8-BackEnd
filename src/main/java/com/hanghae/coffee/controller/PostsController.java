package com.hanghae.coffee.controller;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.dto.posts.PostsRequestDto;
import com.hanghae.coffee.dto.posts.PostsResponseDto;
import com.hanghae.coffee.dto.posts.PostsSliceResponseDto;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.service.posts.PostsTagsService;
import com.hanghae.coffee.service.posts.FileService;
import com.hanghae.coffee.repository.posts.PostsImageRepository;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("api")
@RequiredArgsConstructor
@Slf4j
public class PostsController {

    private final PostsRepository postsRepository;
    private final PostsImageRepository postsImageRepository;
    private final PostsService postsService;
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
    public PostsSliceResponseDto getPost(@PageableDefault(size = 4, sort = "id", direction = Direction.ASC) Pageable pageable,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
//            List<Posts> posts = postsRepository.findAllByOrderByModifiedAtDesc();
//            return postsRepository.findAllByOrderByModifiedAtDesc();
//        List<PostsJoinVO> posts = postsRepository.findAllWithPostImages();
//        List<PostsJoinVO> result = posts.stream()
//            .map(p -> new PostsJoinVO(p))
//            .collect(Collectors.toList());
//        return result;
        Long user_id;
        if(userDetails == null){
            user_id = 0L;
        } else{
            user_id = userDetails.getUser().getId();
        }
        return postsService.getPostList(user_id, pageable);
    }

    // 게시글 세부 조회
    @ResponseBody
    @GetMapping("posts/{post_id}")
    public PostsResponseDto getDetailPost(@PathVariable Long post_id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws RestException{
        Long user_id;
        if(userDetails == null){
            user_id = 0L;
        } else{
            user_id = userDetails.getUser().getId();
        }
        return postsService.getDetailPost(post_id,user_id);
    }

    // 내 게시글 전체 조회
    @ResponseBody
    @GetMapping("posts/mine")
    public PostsSliceResponseDto getMyPost(
        @AuthenticationPrincipal UserDetailsImpl userDetails, @PageableDefault(page = 0,size = 3, sort = "id", direction = Direction.ASC) Pageable pageable) throws RestException{
        Long user_id;
        if(userDetails == null){
            user_id = 0L;
        } else{
            user_id = userDetails.getUser().getId();
        }
        return postsService.getMyPostList(user_id, pageable);
    }

    //게시글 추가
    @ResponseBody
    @PostMapping(value = "posts",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DefaultResponseDto writePost(
        @RequestPart(value = "title") String title,
        @RequestPart(value = "content") String content,
        @RequestPart(value = "tag_name") String tagName,
        @RequestPart(value = "posts_image", required = false) MultipartFile posts_image,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        log.info("writePost");
        log.info(String.valueOf(posts_image));
        Posts posts = postsService.writePost(title,content,userDetails);
        postsTagsService.putPostsTags(posts,tagName);
        if(posts_image != null){
            fileService.uploadFile(posts,posts_image);
        }

        return DefaultResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .build();
    }

    //게시글 수정
    @ResponseBody
    @PostMapping("posts/update")
    public DefaultResponseDto updatePost(Long post_id,
        @RequestPart(value = "title") String title,
        @RequestPart(value = "content") String content,
        @RequestPart(value = "tag_name") String tagName,
        @RequestPart(value = "posts_image") MultipartFile picture,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        Posts posts = postsRepository.findById(post_id).orElseThrow(
            () -> new RestException(HttpStatus.BAD_REQUEST,"게시글 수정 실패")
        );
        Long id = posts.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())){
            postsTagsService.updatePostsTags(posts,tagName);
            fileService.updateFile(posts,picture);
            postsService.updatePost(post_id,title,content,userDetails);
            return DefaultResponseDto
                .builder()
                .status(HttpStatus.OK)
                .msg("success")
                .build();
        } else{
            return DefaultResponseDto
                .builder()
                .status(HttpStatus.FORBIDDEN)
                .msg("forbidden")
                .build();
        }


    }


    // 게시글 삭제
    // 유저 정보 확인 후 권한 확인
    @ResponseBody
    @PostMapping("posts/delete")
    public DefaultResponseDto deletePost(@RequestBody PostsRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long posts_id = requestDto.getPosts_id();

        Posts posts = postsRepository.findById(posts_id).orElseThrow(
            () -> new NullPointerException("fail")
        );
        Long id = posts.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())){
            fileService.deleteFile(posts_id);
            postsRepository.deleteById(posts_id);
            return DefaultResponseDto
                .builder()
                .status(HttpStatus.OK)
                .msg("success")
                .build();
        } else{
            return DefaultResponseDto
                .builder()
                .status(HttpStatus.FORBIDDEN)
                .msg("forbidden")
                .build();
        }

    }


}