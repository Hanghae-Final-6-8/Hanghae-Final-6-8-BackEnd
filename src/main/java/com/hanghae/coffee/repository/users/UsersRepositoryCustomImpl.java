package com.hanghae.coffee.repository.users;

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
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsersRepositoryCustomImpl implements UsersRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CountInfoByUserDto getCountInfoByUser(Long userId) {

        Optional<Long> favorites_count = Optional.ofNullable(
            jpaQueryFactory.select(favorites.users.id.count())
                .from(favorites)
                .where(favorites.users.id.eq(userId))
                .orderBy(OrderByNull.DEFAULT)
                .groupBy(favorites.users.id)
                .fetchOne());

        Optional<Long> posts_count = Optional.ofNullable(
            jpaQueryFactory.select(posts.users.id.count())
                .from(posts)
                .where(posts.users.id.eq(userId))
                .orderBy(OrderByNull.DEFAULT)
                .groupBy(posts.users.id)
                .fetchOne());

        Optional<Long> likes_count =
            Optional.ofNullable(jpaQueryFactory.select(likes.users.id.count())
                .from(likes)
                .where(likes.users.id.eq(userId))
                .orderBy(OrderByNull.DEFAULT)
                .groupBy(likes.users.id)
                .fetchOne());

        favorites_count = favorites_count.isEmpty() ? Optional.of(0L) : favorites_count;
        posts_count = posts_count.isEmpty() ? Optional.of(0L) : posts_count;
        likes_count = likes_count.isEmpty() ? Optional.of(0L) : likes_count;

        return CountInfoByUserDto
            .builder()
            .posts_count(posts_count.get())
            .likes_count(likes_count.get())
            .favorites_count(favorites_count.get())
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
