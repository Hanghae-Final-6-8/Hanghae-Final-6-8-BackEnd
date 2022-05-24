package com.hanghae.coffee.dto.taste;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TasteRequestDto {

    private int acidity;

    private int sweetness;

    private int bitter;

    private int body;

    private int nutty;

    private int floral;

    private int fruit_flavor;

    private int nutty_flavor;

    private int cocoa_flavor;

}
