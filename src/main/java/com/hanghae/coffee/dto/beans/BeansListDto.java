package com.hanghae.coffee.dto.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeansListDto {

    private Long beanId;

    private String beanName;

    private int type;

    private String beanImage;

    private String description;

    public BeansListDto(BeansListDto beansListDto) {

        this.beanId = beansListDto.getBeanId();
        this.beanName = beansListDto.getBeanName();
        this.type = beansListDto.getType();
        this.beanImage = beansListDto.getBeanImage();
        this.description = beansListDto.getDescription();

    }
}
