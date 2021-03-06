package com.hanghae.coffee.repository.beans;

import static com.hanghae.coffee.model.QBeans.beans;
import static com.hanghae.coffee.model.QCafe.cafe;
import static com.hanghae.coffee.model.QFavorites.favorites;

import com.hanghae.coffee.dto.beans.BeansDto;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class BeansRepositoryCustomImpl implements BeansRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<BeansDto> getBeansByBeanId(Long userId, Long beanId) {

        return Optional.ofNullable(jpaQueryFactory
            .select(
                Projections.bean(BeansDto.class,
                    beans.id.as("beanId"),
                    beans.beanName,
                    beans.beanImage,
                    beans.type,
                    beans.acidity,
                    beans.sweetness,
                    beans.bitter,
                    beans.body,
                    beans.nutty,
                    beans.floral,
                    beans.fruitFlavor,
                    beans.cocoaFlavor,
                    beans.nuttyFlavor,
                    cafe.id.as("cafeId"),
                    cafe.cafeName,
                    cafe.cafeLogoImage,
                    cafe.cafeBackGroundImage,
                    beans.description,
                    ExpressionUtils.as(favoritesSubQuery(userId, beanId), "favoritesId"))
            )
            .from(beans)
            .innerJoin(beans.cafe, cafe)
            .where(beans.id.eq(beanId))
            .fetchOne());

    }

    private JPQLQuery<Long> favoritesSubQuery(Long userId, Long beanId) {
        if (ObjectUtils.isEmpty(userId)) {
            return null;
        }
        return JPAExpressions.select(favorites.id.coalesce(0L))
            .from(favorites)
            .where(favorites.users.id.eq(userId)
                .and(favorites.beans.id.eq(beanId)));

    }

    @Override
    public Page<BeansListDto> getBeansList(String type, Pageable pageable) {

        QueryResults<BeansListDto> result = jpaQueryFactory
            .select(
                Projections.bean(BeansListDto.class,
                    beans.id.as("beanId"),
                    beans.beanName,
                    beans.type,
                    beans.beanImage,
                    beans.description)
            )
            .from(beans)
            .where(eqType(type))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());

    }

    private BooleanExpression eqType(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        return beans.type.eq(Integer.parseInt(type));
    }

    @Override
    public List<BeansListDto> getBeansListByKeyword(String keyword) {
        return jpaQueryFactory
            .select(
                Projections.bean(BeansListDto.class,
                    beans.id.as("beanId"),
                    beans.beanName,
                    beans.type,
                    beans.beanImage,
                    beans.description)
            )
            .from(beans)
            .where(beans.beanName.contains(keyword))
            .fetch();
    }


    /**
     * OrderSpecifier ??? ????????? ???????????? ??????????????? ????????????. * ????????? ?????? * @param page * @return
     */
    private OrderSpecifier<?> BeansSort(Pageable page) {
        if (!page.getSort()
            .isEmpty()) { //???????????? ?????? ????????? for ???????????? ?????? ????????????
            for (Sort.Order order : page.getSort()) { // ??????????????? ????????? DESC or ASC ??? ????????????.
                Order direction = order.getDirection().isAscending() ? Order.ASC
                    : Order.DESC; // ??????????????? ????????? ?????? ????????? ????????? ????????? ?????? ???????????? ???????????? ??????.
                switch (order.getProperty()) {
                    case "descending":
                        return new OrderSpecifier(direction, beans.id);

                }
            }
        }
        return null;
    }
}
