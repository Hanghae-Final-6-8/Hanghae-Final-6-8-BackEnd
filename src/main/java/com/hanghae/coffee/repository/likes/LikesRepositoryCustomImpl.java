package com.hanghae.coffee.repository.likes;

import static com.hanghae.coffee.model.QLikes.likes;
import static com.hanghae.coffee.model.QPosts.posts;
import static com.hanghae.coffee.model.QPostsImage.postsImage;
import static com.hanghae.coffee.model.QPostsTags.postsTags;
import static com.hanghae.coffee.model.QTags.tags;

import com.hanghae.coffee.dto.likes.LikesDto;
import com.hanghae.coffee.repository.helper.RepositorySliceHelper;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@RequiredArgsConstructor
public class LikesRepositoryCustomImpl implements LikesRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public Slice<LikesDto> getAllByOrderByUser_IdwithPosts(Long id, Pageable pageable){
    List<LikesDto> postsDtoList = jpaQueryFactory
        .select(
            Projections.fields(LikesDto.class,
                posts.id.as("posts_id"),
                posts.title,
                posts.content,
                posts.users.nickname,
                posts.createdAt,
                posts.modifiedAt,
                postsImage.imageUrl.as("posts_image")
            ))
        .from(posts)
        .leftJoin(likes)
          .on(posts.id.eq(likes.posts.id))
        .leftJoin(posts.postsImages, postsImage)
        .leftJoin(posts.postsTags, postsTags)
        .leftJoin(tags)
          .on(postsTags.tags.id.eq(tags.id))
        .where(likes.users.id.eq(id))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    return RepositorySliceHelper.toSlice(postsDtoList, pageable);
  }

}