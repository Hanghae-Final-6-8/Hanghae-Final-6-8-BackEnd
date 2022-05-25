package com.hanghae.coffee.service.posts;


import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.posts.PostsDto;
import com.hanghae.coffee.dto.posts.PostsRequestDto;
import com.hanghae.coffee.dto.posts.PostsResponseDto;
import com.hanghae.coffee.dto.posts.PostsSliceResponseDto;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
@Slf4j
public class PostsService {

    private final static String DIRECTORY_URL = "posts/images";
    private final PostsRepository postsRepository;
    private final PostsImageService postsImageService;
    private final FileService fileService;
    private final PostsTagsService postsTagsService;

    public PostsSliceResponseDto getPostList(UserDetailsImpl userDetails, Pageable pageable) {
        Long user_id = Optional.ofNullable(userDetails).map(UserDetailsImpl::getUser).map(Users::getId).orElse(0L);

        Slice<PostsDto> postsDtoSlice = postsRepository.getAllWithPostImages(user_id, pageable);


        return PostsSliceResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(postsDtoSlice)
            .build();
    }

    public PostsSliceResponseDto getMyPostList(UserDetailsImpl userDetails, Pageable pageable) {
        Long user_id = Optional.ofNullable(userDetails).map(UserDetailsImpl::getUser).map(Users::getId).orElse(0L);

        Slice<PostsDto> postsDtoSlice = postsRepository.getPostsAllByUsers_Id(user_id, pageable);

        return PostsSliceResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(postsDtoSlice)
            .build();
    }



    public PostsResponseDto getDetailPost(Long post_id, UserDetailsImpl userDetails) {
        Long user_id = Optional.ofNullable(userDetails).map(UserDetailsImpl::getUser).map(Users::getId).orElse(0L);
        PostsDto postsDto = postsRepository.getPostsByIdWithPostImages(post_id, user_id);


        return PostsResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(postsDto)
            .build();
    }

    public Posts writePosts(String title, String content, String tagName, MultipartFile posts_image,
        UserDetailsImpl userDetails) throws IOException {
        List<String> tagNameList = List.of(tagName.split(","));

        Posts posts = new Posts(title, content, userDetails.getUser());

        postsRepository.save(posts);
        postsTagsService.putPostsTags(posts,tagNameList);

        if (posts_image != null) {
            String url = fileService.uploadFile(posts.getId(), posts_image, DIRECTORY_URL);
            postsImageService.imageSave(posts, url);
        }
        return posts;
    }

    @Transactional
    public Posts updatePosts(Long posts_id, String title, String content, String tagName,
        MultipartFile picture, UserDetailsImpl userDetails) throws IOException {
        Posts posts = getPosts(posts_id, userDetails.getUser().getId());
        List<String> tagNameList = List.of(tagName.split(","));

        // 업로드 이미지가 있으면
        if (picture != null) {
            //기존에 저장되어 있는 이미지 찾아오기
            String url = postsImageService.getImageUrl(posts_id);
            //기존 이미지가 있으면
            if (url != null) {
                //이미지 업데이트
                postsImageService.imageDelete(posts_id);
                String newUrl = fileService.updateFile(posts_id, url, picture, DIRECTORY_URL);

                postsImageService.imageSave(posts, newUrl);

            } else {
                //이미지 업로드
                String newUrl = fileService.uploadFile(posts.getId(), picture, DIRECTORY_URL);
                postsImageService.imageSave(posts, newUrl);
            }

        }
        postsTagsService.updatePostsTags(posts, tagNameList);
        posts.update(title, content, userDetails.getUser());
        return posts;
    }

    public Posts getPosts(Long postId, Long userId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(
            () -> new RestException(HttpStatus.BAD_REQUEST, "bad request")
        );

        if (!posts.getUsers().getId().equals(userId)) {

            throw new RestException(HttpStatus.FORBIDDEN, "forbidden");

        }

        return posts;
    }

    public void deletePost(PostsRequestDto requestDto, UserDetailsImpl userDetails) {
        Long postId = requestDto.getPosts_id();

        Posts posts = getPosts(postId, userDetails.getUser().getId());
        String url = postsImageService.getImageUrl(postId);

        if (url != null) {
            fileService.deleteFile(url);
        }

        postsRepository.deleteById(postId);
    }



}

