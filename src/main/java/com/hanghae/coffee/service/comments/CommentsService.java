package com.hanghae.coffee.service.comments;

import com.hanghae.coffee.advice.ErrorCode;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.comments.CommentsDto;
import com.hanghae.coffee.dto.comments.CommentsRequestDto;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.repository.comments.CommentsRepository;
import com.hanghae.coffee.model.Comments;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Slf4j
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;

    public Slice<CommentsDto> getComment(Long posts_id, Pageable pageable) {

        return commentsRepository.getAllByPosts_Id(posts_id,pageable);
    }

    public Slice<CommentsDto> getMyComment(Long id, Pageable pageable) {

        return commentsRepository.getAllByUsers_Id(id, pageable);
    }

    @Transactional
    public CommentsDto writeComment(CommentsRequestDto requestDto, UserDetailsImpl userDetails){
        log.info("writePost");
        Posts posts = postsRepository.findById(requestDto.getPosts_id()).orElseThrow(
            () -> new RestException(ErrorCode.NOT_FOUND_POST)
        );
        Comments comments = new Comments(requestDto.getContent(), posts,userDetails.getUser());
        Comments newcomments = commentsRepository.save(comments);

        return Optional.ofNullable(commentsRepository.getByIdWithDto(newcomments.getId())).orElseThrow(
            () -> new RestException(ErrorCode.NOT_FOUND_COMMENT)
        );

    }

    @Transactional
    public String deleteComment(CommentsRequestDto requestDto,
        UserDetailsImpl userDetails) {
        Comments comments = commentsRepository.findById(requestDto.getComments_id()).orElseThrow(
            () -> new RestException(ErrorCode.NOT_FOUND_COMMENT)
        );
        Long id = comments.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())) {
            commentsRepository.deleteById(requestDto.getComments_id());
            return "success";
        } throw new RestException(ErrorCode.PERMISSION_DENIED_TO_DELETE);

    }


    }

