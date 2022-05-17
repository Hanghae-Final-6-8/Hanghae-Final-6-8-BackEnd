package com.hanghae.coffee.dto.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeansDto {

    private Long beanId;

    private String beanName;

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

    private String cafeImage;

    private String description;

//    @QueryProjection
//    public BeansDto(Beans beans) {
//        this.beanId = beans.getId();
//        this.beanName = beans.getBeanName();
//        this.type = beans.getType();
//        this.acidity = beans.getAcidity();
//        this.sweetness = beans.getSweetness();
//        this.bitter = beans.getBitter();
//        this.body = beans.getBody();
//        this.nutty = beans.getNutty();
//        this.floral = beans.getFloral();
//        this.fruitFlavor = beans.getFruitFlavor();
//        this.nuttyFlavor = beans.getNuttyFlavor();
//        this.cocoaFlavor = beans.getCocoaFlavor();
//        this.description = beans.getDescription();
//        this.cafeId = beans.getCafe().getId();
//        this.cafeName = beans.getCafe().getCafeName();
//        this.cafeImage = beans.getCafe().getCafeImage();
//
//    }

}
