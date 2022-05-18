package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.beans.BeansListResponseDto;
import com.hanghae.coffee.dto.beans.BeansResponseDto;
import com.hanghae.coffee.service.beans.BeansService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/beans")
public class BeansController {

    private final BeansService beansService;

    /**
     * 원두 상세 검색
     *
     */
    @GetMapping("/{beanId}")
    public BeansResponseDto getBeansByBeanId(@PathVariable Long beanId) {

        return beansService.getBeansByBeanId(beanId);

    }
    /**
     * 원두 목록 조회
     *
     */
    @GetMapping("/list")
    public BeansListResponseDto getBeansList(){
        return beansService.getBeansList();
    }

    /**
     * 원두 상세 검색(검색어)
     *
     */
    @GetMapping("/list/{keyword}")
    public BeansListResponseDto getBeansListByKeyword(@PathVariable(required = false) String keyword){
        return beansService.getBeansListByKeyword(keyword);
    }



}
