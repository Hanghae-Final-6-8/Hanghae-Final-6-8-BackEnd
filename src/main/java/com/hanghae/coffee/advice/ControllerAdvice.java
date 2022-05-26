package com.hanghae.coffee.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> exceptionHandler(Exception e) {

        Map<String, Object> resBody = new HashMap<>();
        resBody.put("msg", e.getMessage());

        return new ResponseEntity<>(resBody,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, Object>> RestExceptionHandler(RestException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status", e.getHttpStatus());
        resBody.put("msg", e.getMsg());

        return new ResponseEntity<>(resBody, e.getHttpStatus());
    }

    /**
     * @valid 유효성체크에 통과하지 못하면  MethodArgumentNotValidException 이 발생한다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodValidException(
        MethodArgumentNotValidException e) {
//        log.warn("MethodArgumentNotValidException 발생!!! url:{}, trace:{}",request.getRequestURI(), e.getStackTrace());
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("msg", makeErrorResponse(e.getBindingResult()));

        return new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
    }

    private String makeErrorResponse(BindingResult bindingResult) {
        String detail = "";

        //에러가 있다면
        if (bindingResult.hasErrors()) {
            //DTO에 설정한 meaasge값을 가져온다
            detail = bindingResult.getFieldError().getField()+" 는 " +bindingResult.getFieldError().getDefaultMessage();

        }

        return detail;
    }
}
