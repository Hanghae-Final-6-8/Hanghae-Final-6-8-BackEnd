package com.hanghae.coffee.repository.comments;

import static com.hanghae.coffee.model.QComments.comments;

import static com.hanghae.coffee.model.QPosts.posts;
import static com.hanghae.coffee.model.QUsers.users;

import com.hanghae.coffee.dto.comments.CommentsDto;
import com.hanghae.coffee.repository.common.OrderByNull;
import com.hanghae.coffee.repository.helper.RepositorySliceHelper;
import com.querydsl.core.types.Projections;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@RequiredArgsConstructor
public class CommentsRepositoryCustomImpl implements CommentsRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Slice<CommentsDto> getAllByPosts_Id(Long posts_id, Pageable pageable) {
    List<CommentsDto> commentsDtoList = jpaQueryFactory
        .select(
            Projections.bean(CommentsDto.class,
                comments.id.as("comments_id"),
                comments.content,
                comments.users.nickname.as("nickname"),
                comments.posts.id.as("posts_id"),
                comments.createdAt.as("created_at"),
                comments.modifiedAt.as("modified_at")
                    ))
        .from(comments)
        .innerJoin(comments.users,users)
        .where(comments.posts.id.eq(posts_id))
        .orderBy(comments.modifiedAt.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    return RepositorySliceHelper.toSlice(commentsDtoList, pageable);
  }

  @Override
  public Slice<CommentsDto> getAllByUsers_Id(Long user_id, Pageable pageable) {
    List<CommentsDto> commentsDtoList = jpaQueryFactory
        .select(
            Projections.bean(CommentsDto.class,
                comments.id.as("comments_id"),
                comments.content,
                comments.users.nickname,
                comments.posts.id.as("posts_id"),
                comments.createdAt.as("created_at"),
                comments.modifiedAt.as("modified_at")
            ))
        .from(comments)
        .innerJoin(comments.users,users)
        .innerJoin(comments.posts,posts)
        .where(comments.users.id.eq(user_id))
        .orderBy(comments.modifiedAt.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    return RepositorySliceHelper.toSlice(commentsDtoList, pageable);
  }

  @Override
  public CommentsDto getByIdWithDto(Long id) {
    return jpaQueryFactory
        .select(
            Projections.bean(CommentsDto.class,
                comments.id.as("comments_id"),
                comments.content,
                comments.users.nickname,
                comments.posts.id.as("posts_id"),
                comments.createdAt.as("created_at"),
                comments.modifiedAt.as("modified_at")
            ))
        .from(comments)
        .innerJoin(comments.users,users)
        .where(comments.id.eq(id))
        .orderBy(comments.modifiedAt.asc())
        .orderBy(OrderByNull.DEFAULT)
        .fetchOne();
  }
}