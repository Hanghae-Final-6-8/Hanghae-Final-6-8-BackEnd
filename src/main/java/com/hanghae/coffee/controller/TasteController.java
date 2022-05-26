package com.hanghae.coffee.controller;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.global.ResponseFormat;
import com.hanghae.coffee.dto.taste.TasteRequestDto;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.taste.TasteService;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/taste")
@Api(tags = {"TASTE API"})
public class TasteController {

    private final TasteService tasteService;

    /**
     * 유저의 커피 취향 조사
     */
    @PostMapping(value = "/tests")
    public ResponseEntity<?> doTasteByUser(@RequestBody @Valid TasteRequestDto tasteRequestDto,
        @AuthenticationPrincipal UserDetailsImpl users, BindingResult bindingResult) {

//        if (bindingResult.hasErrors()) {
//            System.out.println(bindingResult);
//            throw new RestException(HttpStatus.BAD_REQUEST, bindingResult.getObjectName());
//
//        }

        tasteService.doTasteByUser(users.getUser(), tasteRequestDto);
        ResponseFormat responseFormat = new ResponseFormat().of(
            tasteService.findTasteByUser(users.getUser()), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * 유저의 커피 취향 조사 결과 확인
     */
    @GetMapping(value = "/tests")
    public ResponseEntity<?> findTasteByUser(@AuthenticationPrincipal UserDetailsImpl users) {

        ResponseFormat responseFormat = new ResponseFormat().of(
            tasteService.findTasteByUser(users.getUser()), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * 유저의 커피 취향과 비슷한 원두 조회
     */
    @GetMapping(value = "/beans")
    public ResponseEntity<?> findTasteByBeans(@AuthenticationPrincipal UserDetailsImpl users) {

        ResponseFormat responseFormat = new ResponseFormat().of(
            tasteService.findTasteListByUserTaste(users.getUser()), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

}
