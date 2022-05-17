package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.beans.BeansListResponseDto;
import com.hanghae.coffee.dto.favorites.FavoritesRequestDto;
import com.hanghae.coffee.dto.favorites.FavoritesResponseDto;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.favorites.FavoritesService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/favorites")
@Api(tags = {"FAVORITES API"})
public class FavoritesController {

    private final FavoritesService favoritesService;

    /**
     * 즐겨찾기 목록 조회
     */
    @GetMapping("/beans")
    public BeansListResponseDto getFavoritesByUser(@AuthenticationPrincipal UserDetailsImpl users) {

        return favoritesService.getFavoritesByUser(users.getUser());

    }

    /**
     * 즐겨찾기 저장
     */
    @PostMapping("/beans")
    public FavoritesResponseDto doFavoritesByUser(@RequestBody FavoritesRequestDto favoritesRequestDto, @AuthenticationPrincipal UserDetailsImpl users) {

        return favoritesService.doFavoritesByUser(favoritesRequestDto.getBean_id(),users.getUser());

    }





}
