package com.hanghae.coffee.repository.beans;

import static com.hanghae.coffee.model.QBeans.beans;
import static com.hanghae.coffee.model.QCafe.cafe;

import com.hanghae.coffee.dto.beans.BeansDto;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.taste.TasteRequestDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BeansCustomRepositoryImpl implements BeansCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<BeansDto> getBeansByBeanId(Long beanId) {

        return Optional.ofNullable(jpaQueryFactory
            .select(
                Projections.bean(BeansDto.class,
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
            .from(beans)
            .innerJoin(beans.cafe, cafe)
            .where(beans.id.eq(beanId))
            .fetchOne());

    }

    @Override
    public List<BeansListDto> getBeansList() {

        return jpaQueryFactory
            .select(
                Projections.bean(BeansListDto.class,
                    beans.id.as("beanId"),
                    beans.beanName,
                    beans.type,
                    beans.description)
            )
            .from(beans)
            .fetch();
    }

    @Override
    public List<BeansDto> getBeansByBeanTaste(TasteRequestDto tasteRequestDto) {
        return jpaQueryFactory
            .select(
                Projections.bean(BeansDto.class,
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
                    beans.description)
            )
            .from(beans)
            .where(beans.acidity.eq(tasteRequestDto.getAcidity())
                .and(beans.sweetness.eq(tasteRequestDto.getSweetness()))
                .and(beans.bitter.eq(tasteRequestDto.getBitter()))
                .and(beans.body.eq(tasteRequestDto.getBody()))
                .and(beans.nutty.eq(tasteRequestDto.getNutty()))
                .and(beans.floral.eq(tasteRequestDto.getFloral()))
                .and(beans.fruitFlavor.eq(tasteRequestDto.getFruit_flavor()))
                .and(beans.cocoaFlavor.eq(tasteRequestDto.getCocoa_flavor()))
                .and(beans.nuttyFlavor.eq(tasteRequestDto.getNutty_flavor()))
            )

            .fetch();

    }

    @Override
    public List<BeansListDto> getBeansListByKeyword(String keyword) {
        return jpaQueryFactory
            .select(
                Projections.bean(BeansListDto.class,
                    beans.id.as("beanId"),
                    beans.beanName,
                    beans.type,
                    beans.description)
            )
            .from(beans)
            .where(beans.beanName.contains(keyword))
            .fetch();
    }
}
