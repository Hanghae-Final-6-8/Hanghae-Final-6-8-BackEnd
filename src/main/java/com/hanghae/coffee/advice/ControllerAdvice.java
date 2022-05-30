package com.hanghae.coffee.advice;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<Map<String, Object>> RestExceptionHandler(RestException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status", e.getHttpStatus());
        resBody.put("msg", e.getMsg());

        return new ResponseEntity<>(resBody, e.getHttpStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> MaxUploadSizeExceededExceptionHandler(
        MaxUploadSizeExceededException e) {
        Map<String, Object> resBody = new HashMap<>();
        resBody.put("status","500");
        resBody.put("msg", "최대 첨부 가능한 사이즈(8MB)를 초과하였습니다.");

        return new ResponseEntity<>(resBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @valid 유효성체크에 통과하지 못하면  MethodArgumentNotValidException 이 발생한다.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,
        MissingServletRequestPartException.class,
        MissingServletRequestParameterException.class
    })
    public ResponseEntity<Map<String, Object>> methodValidException(Exception e) {
        Map<String, Object> resBody = new HashMap<>();
        if (e instanceof MethodArgumentNotValidException) {
            resBody.put("msg", makeErrorResponse(((MethodArgumentNotValidException) e).getBindingResult()));
        }else if(e instanceof MissingServletRequestPartException){
            resBody.put("msg", ((MissingServletRequestPartException) e).getRequestPartName() + " error");
        }else if(e instanceof MissingServletRequestParameterException){
            resBody.put("msg", ((MissingServletRequestParameterException) e).getParameterName()+ " 을 찾을 수 없습니다.");
        }

        return new ResponseEntity<>(resBody, HttpStatus.BAD_REQUEST);
    }

    private String makeErrorResponse(BindingResult bindingResult) {
        String detail = "";

        //에러가 있다면
        if (bindingResult.hasErrors()) {
            //DTO에 설정한 meaasge값을 가져온다
            detail = bindingResult.getFieldError().getField() + " 는 " + bindingResult.getFieldError()
                    .getDefaultMessage();

        }

        return detail;
    }
}
