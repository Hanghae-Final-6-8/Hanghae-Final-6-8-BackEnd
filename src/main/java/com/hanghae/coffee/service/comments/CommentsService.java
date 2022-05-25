package com.hanghae.coffee.service.comments;
import com.hanghae.coffee.advice.RestException;

import com.hanghae.coffee.dto.comments.CommentsRequestDto;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.repository.comments.CommentsRepository;
import com.hanghae.coffee.model.Comments;
import com.hanghae.coffee.repository.posts.PostsRepository;
import com.hanghae.coffee.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostsRepository postsRepository;


    public Slice<Comments> getComment(Long posts_id, Pageable pageable) {

        return commentsRepository.findAllByPosts_Id(posts_id,pageable);
    }


    public Slice<Comments> getMyComment(Long id, Pageable pageable) {

        return commentsRepository.findAllByUsers_Id(id, pageable);
    }

    public Comments writeComment(CommentsRequestDto requestDto, UserDetailsImpl userDetails){
        log.info("writePost");
        Posts posts = postsRepository.findById(requestDto.getPosts_id()).orElseThrow(
            () -> new RestException(HttpStatus.BAD_REQUEST,"bad request")
        );
        Comments comments = new Comments(requestDto.getContent(), posts,userDetails.getUser());
        Comments newcomments = commentsRepository.save(comments);

        return commentsRepository.findById(newcomments.getId()).orElseThrow(
            () -> new RestException(HttpStatus.BAD_REQUEST,"bad request")
        );

    }

    public String deleteComment(CommentsRequestDto requestDto,
        UserDetailsImpl userDetails) {
        Comments comments = commentsRepository.findById(requestDto.getComments_id()).orElseThrow(
            () -> new NullPointerException("fail")
        );
        Long id = comments.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())) {
            commentsRepository.deleteById(requestDto.getComments_id());
            return "success";
        } throw new RestException(HttpStatus.FORBIDDEN,"forbidden");

    }


    }

