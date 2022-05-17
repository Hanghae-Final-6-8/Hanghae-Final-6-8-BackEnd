package com.hanghae.coffee.dto.global;

import java.util.ArrayList;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@SuperBuilder
public class DefaultResponseDto {

    private HttpStatus status;
    private String msg;

    protected DefaultResponseDto(HttpStatus status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    protected DefaultResponseDto() {
    }
}