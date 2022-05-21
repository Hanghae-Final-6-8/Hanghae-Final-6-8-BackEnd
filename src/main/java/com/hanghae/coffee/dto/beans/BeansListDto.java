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

}
