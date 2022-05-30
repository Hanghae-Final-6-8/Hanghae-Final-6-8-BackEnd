package com.hanghae.coffee.model;

import static javax.persistence.FetchType.LAZY;

import com.hanghae.coffee.dto.beans.BeansDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Beans {

    @Id
    @Column(name = "bean_id")
    private Long id;

    @Column(nullable = false)
    private String beanName;

    @Column(length = 1000)
    private String beanImage;

    @Column(nullable = false)
    private int type;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '산미'")
    private int acidity;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '단 맛'")
    private int sweetness;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '쓴 맛'")
    private int bitter;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '바디감'")
    private int body;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '고소한 맛맛'")
    private int nutty;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '꽃 향'")
    private int floral;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '과일 향'")
    private int fruitFlavor;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '견과류 향'")
    private int nuttyFlavor;

    @Column(columnDefinition = " INT(1) NOT NULL COMMENT '코코아 향'")
    private int cocoaFlavor;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;


    @Builder
    public Beans(BeansDto beansDto, Cafe cafe) {
        this.id = beansDto.getBeanId();
        this.beanName = beansDto.getBeanName();
        this.beanImage = beansDto.getBeanImage();
        this.type = beansDto.getType();
        this.acidity = beansDto.getAcidity();
        this.sweetness = beansDto.getSweetness();
        this.body = beansDto.getBody();
        this.bitter = beansDto.getBitter();
        this.nutty = beansDto.getNutty();
        this.floral = beansDto.getFloral();
        this.fruitFlavor = beansDto.getFruitFlavor();
        this.cocoaFlavor = beansDto.getCocoaFlavor();
        this.nuttyFlavor = beansDto.getNuttyFlavor();
        this.description = beansDto.getDescription();
        this.cafe = cafe;
    }

}
