package com.hanghae.coffee.dto.global;

import java.util.ArrayList;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class DefaultResponseDto {

    private Integer status;
    private String msg;

    protected DefaultResponseDto(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    protected DefaultResponseDto() {
    }
}