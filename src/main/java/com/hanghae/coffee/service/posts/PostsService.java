package com.hanghae.coffee.service.posts;

import com.hanghae.coffee.advice.ErrorCode;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.posts.PostsDto;
import com.hanghae.coffee.dto.posts.PostsRequestDto;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
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

    public Slice<PostsDto> getPostList(UserDetailsImpl userDetails, Pageable pageable) {
        Long user_id = Optional.ofNullable(userDetails).map(UserDetailsImpl::getUser).map(Users::getId).orElse(0L);

        return postsRepository.getAllWithPostImages(user_id, pageable);
    }

    public Slice<PostsDto>  getMyPostList(UserDetailsImpl userDetails, Pageable pageable) {
        Long user_id = Optional.ofNullable(userDetails).map(UserDetailsImpl::getUser).map(Users::getId).orElse(0L);

        return postsRepository.getPostsAllByUsers_Id(user_id, pageable);
    }



    public PostsDto getDetailPost(Long post_id, UserDetailsImpl userDetails) {
        Long user_id = Optional.ofNullable(userDetails).map(UserDetailsImpl::getUser).map(Users::getId).orElse(0L);

        return postsRepository.getPostsByIdWithPostImages(post_id, user_id);
    }

    @Transactional
    public Posts writePosts(String title, String content, String tagName, MultipartFile posts_image,
        UserDetailsImpl userDetails) {

        Posts posts = new Posts(title, content, userDetails.getUser());
        postsRepository.save(posts);

        if(Optional.ofNullable(tagName).isPresent()) {
            List<String> tagNameList = List.of(tagName.split(","));
            postsTagsService.putPostsTags(posts,tagNameList);
        }




        Optional<MultipartFile> multipartFile = Optional.ofNullable(posts_image);
        if (multipartFile.isPresent()) {
            try {
                String url = fileService.uploadFile(posts.getId(), multipartFile.get(), DIRECTORY_URL);
                postsImageService.imageSave(posts, url);
            } catch (IOException e) {
                throw new RestException(ErrorCode.COMMON_BAD_REQUEST_400_FILE);
            }
        }
        return posts;
    }

    @Transactional
    public Posts updatePosts(Long posts_id, String title, String content, String tagName,
        MultipartFile picture, UserDetailsImpl userDetails){
        Posts posts = getPosts(posts_id, userDetails.getUser().getId());
        posts.update(title, content, userDetails.getUser());
        if(Optional.ofNullable(tagName).isPresent()) {
            List<String> tagNameList = List.of(tagName.split(","));
            postsTagsService.updatePostsTags(posts, tagNameList);

        }


        Optional<MultipartFile> multipartFile = Optional.ofNullable(picture);
        // 업로드 이미지가 있으면
        if (multipartFile.isPresent()) {
            //기존에 저장되어 있는 이미지 찾아오기
            Optional<String> url = postsImageService.getImageUrl(posts_id);
            //기존 이미지가 있으면
            if (url.isPresent()) {
                try {
                    //이미지 업데이트
                    postsImageService.imageDelete(posts_id);
                    String newUrl = fileService.updateFile(posts_id, url.get(), multipartFile.get(), DIRECTORY_URL);
                    postsImageService.imageSave(posts, newUrl);
                } catch (IOException e) {
                    throw new RestException(ErrorCode.COMMON_BAD_REQUEST_400_FILE);
                }

            } else {
                try {
                    //이미지 업로드
                    String newUrl = fileService.uploadFile(posts.getId(), picture, DIRECTORY_URL);
                    postsImageService.imageSave(posts, newUrl);
                } catch (IOException e) {
                    throw new RestException(ErrorCode.COMMON_BAD_REQUEST_400_FILE);
                }
            }
        }
        return posts;
    }

    public Posts getPosts(Long postId, Long userId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(
            () -> new RestException(ErrorCode.NOT_FOUND_POST)
        );
        if (!posts.getUsers().getId().equals(userId)) {throw new RestException(ErrorCode.PERMISSION_DENIED);}

        return posts;
    }


    @Transactional
    public void deletePost(PostsRequestDto requestDto, UserDetailsImpl userDetails) {

        Long postId = requestDto.getPosts_id();

        Posts posts = getPosts(postId, userDetails.getUser().getId());

        Optional<String> url = postsImageService.getImageUrl(posts.getId());
        postsRepository.deleteById(posts.getId());

        url.ifPresent(fileService::deleteFile);



    }

}

