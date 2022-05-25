package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.global.ResponseFormat;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.beans.BeansService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/beans")
public class BeansController {

    private final BeansService beansService;

    /**
     * 원두 상세 검색
     */
    @GetMapping("/{beanId}")
    public ResponseEntity<?> getBeansByBeanId(@AuthenticationPrincipal UserDetailsImpl users,
        @PathVariable Long beanId) {
        Long userId = 0L;
        try {
            userId = users.getUser().getId();
        } catch (NullPointerException e) {
            userId = 0L;
        }

        ResponseFormat responseFormat = new ResponseFormat().of(
            beansService.getBeansByBeanId(userId, beanId), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * 원두 목록 조회
     */
    @GetMapping("/list")
    public ResponseEntity<?> getBeansList(Pageable pageable,
        @RequestParam(value = "type", defaultValue = "") String type) {
        ResponseFormat responseFormat = new ResponseFormat().of(
            beansService.getBeansList(type, pageable), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);
    }

    /**
     * 원두 상세 검색(검색어)
     */
    @GetMapping("/list/{keyword}")
    public ResponseEntity<?> getBeansListByKeyword(@PathVariable(required = false) String keyword) {
        ResponseFormat responseFormat = new ResponseFormat().of(
            beansService.getBeansListByKeyword(keyword), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);
    }


}
