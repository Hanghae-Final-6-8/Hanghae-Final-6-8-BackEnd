package com.hanghae.coffee.dto.taste;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TasteDto {

    private Long beanId;

    private String beanName;

    private String beanImage;

    private int type;

    private int acidity;

    private int sweetness;

    private int bitter;

    private int body;

    private int nutty;

    private int floral;

    private int fruitFlavor;

    private int nuttyFlavor;

    private int cocoaFlavor;

    private Long cafeId;

    private String cafeName;

    private String cafeLogoImage;

    private String cafeBackGroundImage;

    private String description;

}
