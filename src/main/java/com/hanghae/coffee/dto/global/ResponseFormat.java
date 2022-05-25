package com.hanghae.coffee.dto.global;

import lombok.Getter;

@Getter
public class ResponseFormat {
    private Object data;
    private String msg;

    public ResponseFormat of(String msg){
        this.data = "";
        this.msg = msg;
        return this;
    }

    public ResponseFormat of(Object data,String msg){
        this.data = data;
        this.msg = msg;
        return this;
    }
}
