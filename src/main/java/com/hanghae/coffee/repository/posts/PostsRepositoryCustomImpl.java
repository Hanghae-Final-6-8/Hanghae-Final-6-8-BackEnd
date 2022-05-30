package com.hanghae.coffee.repository.posts;

import static com.hanghae.coffee.model.QPosts.posts;
import static com.hanghae.coffee.model.QLikes.likes;
import static com.hanghae.coffee.model.QUsers.users;
import static com.hanghae.coffee.model.QPostsTags.postsTags;
import static com.hanghae.coffee.model.QTags.tags;
import static com.hanghae.coffee.model.QPostsImage.postsImage;

import com.hanghae.coffee.dto.posts.PostsDto;
import com.hanghae.coffee.repository.common.OrderByNull;
import com.hanghae.coffee.repository.helper.RepositorySliceHelper;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@RequiredArgsConstructor
public class PostsRepositoryCustomImpl implements PostsRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Slice<PostsDto> getPostsAllByUsers_Id(Long id, Pageable pageable) {
    List<PostsDto> postsDtoList = jpaQueryFactory
        .select(
            Projections.bean(PostsDto.class,
                posts.id.as("posts_id"),
                posts.title,
                posts.content,
                posts.users.nickname,
                posts.createdAt.as("created_at"),
                posts.modifiedAt.as("modified_at"),
                postsImage.imageUrl.as("posts_image"),
                ExpressionUtils.
                    as(JPAExpressions
                        .select(likes.posts.id.count())
                        .from(likes)
                        .where(likes.posts.id.eq(posts.id))
                        .groupBy(likes.posts.id)
                        .orderBy(OrderByNull.DEFAULT),
                        "likes_count"),
                ExpressionUtils.
                    as(JPAExpressions
                            .select(likes.users.id)
                            .from(likes)
                            .where(posts.id.eq(likes.posts.id))
                            .where(likes.users.id.eq(id)),
                        "isLikes"),
                ExpressionUtils.
                    as(JPAExpressions
                            .select(Expressions.stringTemplate("group_concat({0})", tags.tagName)
                            )
                            .from(postsTags)
                            .innerJoin(postsTags.tags, tags)
                            .where(posts.id.eq(postsTags.posts.id))
                            .groupBy(posts.id)
                            .orderBy(OrderByNull.DEFAULT),
                        "tag_name")
            ))
        .from(posts)
        .leftJoin(posts.postsImages, postsImage)
        .innerJoin(posts.users,users)
        .where(posts.users.id.eq(id))
        .orderBy(posts.modifiedAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    return RepositorySliceHelper.toSlice(postsDtoList, pageable);
  }

  @Override
  public Slice<PostsDto> getAllWithPostImages(Long id, Pageable pageable) {
    List<PostsDto> postsDtoList = jpaQueryFactory
        .select(
            Projections.bean(PostsDto.class,
                posts.id.as("posts_id"),
                posts.title,
                posts.content,
                posts.users.nickname,
                posts.createdAt.as("created_at"),
                posts.modifiedAt.as("modified_at"),
                postsImage.imageUrl.as("posts_image")
                ,
                ExpressionUtils.
                    as(JPAExpressions
                        .select(likes.posts.id.count())
                        .from(likes)
                        .where(likes.posts.id.eq(posts.id))
                        .groupBy(likes.posts.id)
                        .orderBy(OrderByNull.DEFAULT),
                        "likes_count"
                    ),
                ExpressionUtils.
                    as(JPAExpressions
                            .select(likes.users.id)
                            .from(likes)
                            .where(posts.id.eq(likes.posts.id))
                            .where(likes.users.id.eq(id)),
                        "isLikes"),

                ExpressionUtils.
                    as(JPAExpressions
                            .select(Expressions.stringTemplate("group_concat({0})", tags.tagName)
                            )
                            .from(postsTags)
                            .innerJoin(postsTags.tags, tags)
                            .where(posts.id.eq(postsTags.posts.id))
                            .groupBy(posts.id)
                            .orderBy(OrderByNull.DEFAULT),
                        "tag_name")
            ))
        .from(posts)
        .leftJoin(posts.postsImages, postsImage)
        .innerJoin(posts.users,users)
        .orderBy(posts.modifiedAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    return RepositorySliceHelper.toSlice(postsDtoList, pageable);
  }

  @Override
  public PostsDto getPostsByIdWithPostImages(Long id, Long user_id) {
    return jpaQueryFactory
        .select(
            Projections.bean(PostsDto.class,
                posts.id.as("posts_id"),
                posts.title,
                posts.content,
                posts.users.nickname,
                posts.createdAt.as("created_at"),
                posts.modifiedAt.as("modified_at"),
                postsImage.imageUrl.as("posts_image"),
                ExpressionUtils.
                    as(JPAExpressions
                        .select(likes.posts.id.count())
                        .from(likes)
                        .where(likes.posts.id.eq(posts.id))
                        .groupBy(likes.posts.id)
                        .orderBy(OrderByNull.DEFAULT),
                        "likes_count"
                    ),
                ExpressionUtils.
                    as(JPAExpressions
                            .select(likes.users.id)
                            .from(likes)
                            .where(posts.id.eq(likes.posts.id))
                            .where(likes.users.id.eq(user_id)),
                        "isLikes"),
                ExpressionUtils.
                    as(JPAExpressions
                            .select(Expressions.stringTemplate("group_concat({0})", tags.tagName)
                            )
                            .from(postsTags)
                            .innerJoin(postsTags.tags, tags)
                            .where(posts.id.eq(postsTags.posts.id))
                            .groupBy(posts.id)
                            .orderBy(OrderByNull.DEFAULT),
                        "tag_name")
            ))
        .from(posts)
        .leftJoin(posts.postsImages, postsImage)
        .where(posts.id.eq(id))
        .orderBy(posts.modifiedAt.desc())
        .orderBy(OrderByNull.DEFAULT)
        .fetchOne();
  }
}