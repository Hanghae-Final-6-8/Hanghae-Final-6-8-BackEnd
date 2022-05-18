package com.hanghae.coffee.service;


import com.hanghae.coffee.dto.comments.CommentsInterfaceJoinVO;
import com.hanghae.coffee.repository.CommentsRepository;
import com.hanghae.coffee.dto.comments.CommentsSliceResponseDto;
import com.hanghae.coffee.model.Comments;
import com.hanghae.coffee.model.Posts;
import com.hanghae.coffee.security.UserDetailsImpl;
import java.io.IOException;
import java.util.Map;
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

    public Comments writeComment(String content, Long post_id, UserDetailsImpl userDetails) throws IOException {
        log.info("writePost");
        Posts posts = new Posts(post_id);
        Comments comments = new Comments(content,posts,userDetails.getUser());

        return commentsRepository.save(comments);

    }

    private String deleteComment(Map<String, Long> param, UserDetailsImpl userDetails) {
        Long comments_id = (Long) param.get("comments_id");

        Comments comments = commentsRepository.findById(comments_id).orElseThrow(
            () -> new NullPointerException("fail")
        );
        Long id = comments.getUsers().getId();

        if(id.equals( userDetails.getUser().getId())){
            commentsRepository.deleteById(comments_id);
        } else{
            return "forbidden";
        }
        return null;
    }


    }

