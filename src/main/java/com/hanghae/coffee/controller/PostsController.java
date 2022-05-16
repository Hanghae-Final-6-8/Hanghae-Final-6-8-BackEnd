package com.hanghae.coffee.controller;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.PostsInterfaceJoinVO;
import com.hanghae.coffee.service.posts.PostsTagsService;
import com.hanghae.coffee.service.posts.FileService;
import com.hanghae.coffee.repository.posts.PostsImageRepository;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    // 게시글 목록 조회
    @ResponseBody
    @GetMapping("posts")
    public List<PostsInterfaceJoinVO> getPost(){
//            List<Posts> posts = postsRepository.findAllByOrderByModifiedAtDesc();
//            return postsRepository.findAllByOrderByModifiedAtDesc();
//        List<PostsJoinVO> posts = postsRepository.findAllWithPostImages();
//        List<PostsJoinVO> result = posts.stream()
//            .map(p -> new PostsJoinVO(p))
//            .collect(Collectors.toList());
//        return result;
        return postsRepository.findAllWithPostImages();
    }

    // 게시글 세부 조회
    @ResponseBody
    @GetMapping("posts/{post_id}")
    public PostsInterfaceJoinVO getDetailPost(@PathVariable Long post_id) throws RestException{
        return postsService.getPost(post_id);
    }

    // 내 게시글 전체 조회
    @ResponseBody
    @GetMapping("posts/mine")
    public List<PostsInterfaceJoinVO> getMyPost(
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws RestException{
        return postsRepository.findAllByUsers_Id(userDetails.getUser().getId());
    }

    //게시글 추가
    @ResponseBody
    @PostMapping(value = "posts",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long writePost(
            @RequestPart(value = "title") String title,
            @RequestPart(value = "content") String content,
            @RequestPart(value = "tag_name") String tagName,
        @RequestPart(value = "picture") MultipartFile picture,
        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        log.info("writePost");
        Posts posts = postsService.writePost(title,content,userDetails);
        postsTagsService.putPostsTags(posts,tagName);
        fileService.uploadFile(posts,picture);

        return posts.getId();
    }

    //게시글 수정
    @ResponseBody
    @PostMapping("posts/update")
    public String updatePost(Long post_id,
                           @RequestPart(value = "title") String title,
                           @RequestPart(value = "content") String content,
                           @RequestPart(value = "tag_name") String tagName,
                           @RequestPart(value = "picture") MultipartFile picture,
                           @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        Posts posts = postsRepository.findById(post_id).orElseThrow(
            () -> new NullPointerException("fail")
        );
        Long id = posts.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())){
            postsTagsService.updatePostsTags(posts,tagName);
            fileService.updateFile(posts,picture);
            postsService.updatePost(post_id,title,content,userDetails);
            return "success";
        } else{
            return "forbidden";
        }


    }


    // 게시글 삭제
    // 유저 정보 확인 후 권한 확인
    @ResponseBody
    @PostMapping("posts/delete")
    public String deletePost(@RequestBody Map<String, Long> param,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long posts_id = (Long) param.get("posts_id");

        Posts posts = postsRepository.findById(posts_id).orElseThrow(
                () -> new NullPointerException("fail")
        );
        Long id = posts.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())){
            fileService.deleteFile(posts_id);
            postsRepository.deleteById(posts_id);
        } else{
            return "forbidden";
        }
            return "success";
    }
}
