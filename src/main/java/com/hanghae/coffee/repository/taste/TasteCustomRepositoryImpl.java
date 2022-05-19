package com.hanghae.coffee.repository.taste;

import static com.hanghae.coffee.model.QBeans.beans;
import static com.hanghae.coffee.model.QCafe.cafe;
import static com.hanghae.coffee.model.QTaste.taste;

import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.taste.TasteDto;
import com.hanghae.coffee.repository.common.OrderByNull;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TasteCustomRepositoryImpl implements TasteCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TasteDto> findTasteByUser(Long userId) {
        TasteDto tasteDto = jpaQueryFactory
            .select(
                Projections.bean(TasteDto.class,
                    beans.id.as("beanId"),
                    beans.beanName,
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
                    cafe.cafeImage,
                    beans.description)
            )
            .from(taste)
            .innerJoin(taste.beans, beans)
            .innerJoin(beans.cafe, cafe)
            .where(taste.users.id.eq(userId))
            .fetchOne();

        return Optional.ofNullable(tasteDto);

    }

    @Override
    public List<BeansListDto> findTasteListByUserTaste(TasteDto tasteDto) {

        return jpaQueryFactory
            .select(
                Projections.bean(BeansListDto.class,
                    beans.id.as("beanId"),
                    beans.beanName,
                    beans.type,
                    beans.description)
            ).
            from(beans)
            .where(
                (beans.floral.eq(tasteDto.getFloral()))
                .and(beans.fruitFlavor.eq(tasteDto.getFruitFlavor()))
                .and(beans.cocoaFlavor.eq(tasteDto.getCocoaFlavor()))
                .and(beans.nuttyFlavor.eq(tasteDto.getNuttyFlavor()))
                .and((beans.acidity.eq(tasteDto.getAcidity())
                .or(beans.sweetness.eq(tasteDto.getSweetness()))
                .or(beans.bitter.eq(tasteDto.getBitter()))
                .or(beans.body.eq(tasteDto.getBody()))
                .or(beans.nutty.eq(tasteDto.getNutty())))

                ))
            .orderBy(OrderByNull.DEFAULT)
            .limit(4)
            .fetch();

    }

}
