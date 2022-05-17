package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.beans.BeansListResponseDto;
import com.hanghae.coffee.dto.cafe.CafeResponseDto;
import com.hanghae.coffee.service.cafe.CafeService;
import lombok.RequiredArgsConstructor;
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
     * cafe 목록 조회
     */
    @GetMapping("/list")
    public CafeResponseDto getCafeList() {

        return cafeService.getCafeList();

    }

    /**
     *
     * cafe 기반 원두 검색
     */
    @GetMapping("/{cafeId}/beans")
    public BeansListResponseDto getBeansList(@PathVariable Long cafeId) {

        return cafeService.getBeansListByCafeId(cafeId);

    }


}
