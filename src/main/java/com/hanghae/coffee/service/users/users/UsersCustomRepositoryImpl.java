package com.hanghae.coffee.service.users.users;

import static com.hanghae.coffee.model.QFavorites.favorites;
import static com.hanghae.coffee.model.QLikes.likes;
import static com.hanghae.coffee.model.QPosts.posts;
import static com.hanghae.coffee.model.QTaste.taste;
import static com.hanghae.coffee.model.QUsers.users;

import com.hanghae.coffee.dto.users.CountInfoByUserDto;
import com.hanghae.coffee.dto.users.UserAuthDto;
import com.hanghae.coffee.repository.common.OrderByNull;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsersCustomRepositoryImpl implements UsersCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CountInfoByUserDto getCountInfoByUser(Long userId) {

        long favorites_count = jpaQueryFactory.selectFrom(favorites)
            .where(favorites.users.id.eq(userId))
            .orderBy(OrderByNull.DEFAULT)
            .groupBy(favorites.users.id)
            .fetch().size();

        long posts_count = jpaQueryFactory.selectFrom(posts)
            .where(posts.users.id.eq(userId))
            .orderBy(OrderByNull.DEFAULT)
            .groupBy(posts.users.id)
            .fetch().size();

        long likes_count = jpaQueryFactory.selectFrom(likes)
            .where(likes.users.id.eq(userId))
            .orderBy(OrderByNull.DEFAULT)
            .groupBy(likes.users.id)
            .fetch().size();

        return CountInfoByUserDto
            .builder()
            .posts_count(posts_count)
            .likes_count(likes_count)
            .favorites_count(favorites_count)
            .build();
    }

    public UserAuthDto getUserAuth(Long userId) {
        return jpaQueryFactory
            .select(Projections.bean(UserAuthDto.class,
                users.nickname,
                users.profileUrl.as("profile_url"),
                ExpressionUtils.as(
                    JPAExpressions.select(taste.id.coalesce(0L))
                        .from(taste)
                        .where(taste.users.id.eq(userId)),
                    "tasteId")
                    ))
            .from(users)
            .where(users.id.eq(userId))
            .fetchOne();
    }


}
