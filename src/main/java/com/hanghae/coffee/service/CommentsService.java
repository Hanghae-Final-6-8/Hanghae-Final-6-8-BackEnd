package com.hanghae.coffee.service;


import com.hanghae.coffee.dto.comments.CommentsInterfaceJoinVO;
import com.hanghae.coffee.dto.comments.CommentsRequestDto;
import com.hanghae.coffee.dto.global.DefaultResponseDto;
import com.hanghae.coffee.repository.CommentsRepository;
import com.hanghae.coffee.dto.comments.CommentsSliceResponseDto;
import com.hanghae.coffee.model.Comments;
import com.hanghae.coffee.security.UserDetailsImpl;
import java.io.IOException;
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


    public CommentsSliceResponseDto getComment(Long posts_id, Pageable pageable) {

        Slice<CommentsInterfaceJoinVO> commentsInterfaceJoinVOSlice = commentsRepository.findAllByPosts_Id(posts_id,pageable);

        return CommentsSliceResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(commentsInterfaceJoinVOSlice)
            .build();
    }


    public CommentsSliceResponseDto getMyComment(Long id, Pageable pageable) {

        Slice<CommentsInterfaceJoinVO> commentsInterfaceJoinVOSlice = commentsRepository.findAllByUsers_Id(id, pageable);

        return CommentsSliceResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(commentsInterfaceJoinVOSlice)
            .build();
    }

    public Comments writeComment(CommentsRequestDto requestDto, UserDetailsImpl userDetails) throws IOException {
        log.info("writePost");
        Comments comments = new Comments(requestDto.getContent(), requestDto.getPosts(),userDetails.getUser());

        return commentsRepository.save(comments);

    }

    private DefaultResponseDto deleteComment(CommentsRequestDto requestDto, UserDetailsImpl userDetails) {
        Comments comments = commentsRepository.findById(requestDto.getComments_id()).orElseThrow(
            () -> new NullPointerException("fail")
        );
        Long id = comments.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())){
            commentsRepository.deleteById(requestDto.getComments_id());
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

