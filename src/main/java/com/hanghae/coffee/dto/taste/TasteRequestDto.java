package com.hanghae.coffee.dto.taste;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TasteRequestDto {

    @Min(1)
    @Max(3)
    private int acidity;

    @Min(1)
    @Max(3)
    private int sweetness;

    @Min(1)
    @Max(3)
    private int bitter;

    @Min(1)
    @Max(3)
    private int body;

    @Min(1)
    @Max(3)
    private int nutty;

    @Min(0)
    @Max(1)
    private int floral;

    @Min(0)
    @Max(1)
    private int fruit_flavor;

    @Min(0)
    @Max(1)
    private int nutty_flavor;

    @Min(0)
    @Max(1)
    private int cocoa_flavor;

}
