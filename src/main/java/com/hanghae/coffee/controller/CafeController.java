package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.global.ResponseFormat;
import com.hanghae.coffee.service.cafe.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/cafe")
public class CafeController {

    private final CafeService cafeService;

    /**
     *
     * 카페 목록 조회
     */
    @GetMapping("/list")
    public ResponseEntity<?> getCafeList() {

        ResponseFormat responseFormat = new ResponseFormat().of(cafeService.getCafeList(), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     *
     * 카페 기준 원두 조회
     */
    @GetMapping("/{cafeId}/beans")
    public ResponseEntity<?> getBeansList(@PathVariable Long cafeId) {

        ResponseFormat responseFormat = new ResponseFormat().of(cafeService.getBeansListByCafeId(cafeId), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }
    /**
     * 카페 기준 원두 종류 수 조회
     */
    @GetMapping("/beans/total")
    public ResponseEntity<?> getBeansTotalByCafe() {

        ResponseFormat responseFormat = new ResponseFormat().of(cafeService.getBeansTotalByCafe(), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }


}
