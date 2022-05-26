package com.hanghae.coffee.dto.favorites;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoritesRequestDto {

    @NotNull(message = "필수값 입니다.")
    private Long bean_id;

}
