package com.hanghae.coffee.dto.cafe;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CafeDto {

    private Long cafeId;

    private String cafeName;

    private String cafeLogoImage;

    private String cafeBackGroundImage;

}
