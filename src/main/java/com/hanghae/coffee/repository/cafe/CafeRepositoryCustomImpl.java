package com.hanghae.coffee.repository.cafe;

import static com.hanghae.coffee.model.QBeans.beans;
import static com.hanghae.coffee.model.QCafe.cafe;
import static com.hanghae.coffee.model.QLikes.likes;
import static com.hanghae.coffee.model.QTaste.taste;

import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.cafe.CafeDto;
import com.hanghae.coffee.repository.common.OrderByNull;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CafeRepositoryCustomImpl implements CafeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CafeDto> getBeansTotalByCafe() {
        return jpaQueryFactory.select(
                Projections.bean(CafeDto.class,
                    cafe.id.as("cafeId"),
                    cafe.cafeName,
                    cafe.cafeLogoImage,
                    cafe.cafeBackGroundImage,
                    ExpressionUtils.as(JPAExpressions.select(beans.count()).from(beans)
                        .where(cafe.id.eq(beans.cafe.id)).groupBy(cafe.id), "beansCount")
                )
            )
            .from(cafe)
            .fetch();
    }
}
