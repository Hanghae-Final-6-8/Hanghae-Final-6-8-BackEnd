package com.hanghae.coffee.advice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;
    private String msg;

    public RestException(ErrorCode errorCode) {
        this.msg = errorCode.getMsg();
        this.httpStatus = errorCode.getHttpStatus();
    }

    public RestException(HttpStatus httpStatus, String msg) {
        this.httpStatus = httpStatus;
        this.msg = msg;
    }
}
