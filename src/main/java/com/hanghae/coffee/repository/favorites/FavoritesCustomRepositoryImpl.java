package com.hanghae.coffee.repository.favorites;

import static com.hanghae.coffee.model.QBeans.beans;
import static com.hanghae.coffee.model.QFavorites.favorites;

import com.hanghae.coffee.dto.beans.BeansListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FavoritesCustomRepositoryImpl implements FavoritesCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BeansListDto> getFavoritesByUser(Long userId) {
        return jpaQueryFactory
            .select(
                Projections.bean(BeansListDto.class,
                    beans.id.as("beanId"),
                    beans.beanName,
                    beans.type,
                    beans.beanImage,
                    beans.description)
            )
            .from(favorites)
            .innerJoin(favorites.beans, beans)
            .where(favorites.users.id.eq(userId))
            .fetch();

    }

}
