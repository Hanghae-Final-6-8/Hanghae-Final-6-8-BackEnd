package com.hanghae.coffee.controller;

import com.hanghae.coffee.dto.favorites.FavoritesRequestDto;
import com.hanghae.coffee.dto.global.ResponseFormat;
import com.hanghae.coffee.security.UserDetailsImpl;
import com.hanghae.coffee.service.favorites.FavoritesService;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getFavoritesByUser(@AuthenticationPrincipal UserDetailsImpl users) {

        ResponseFormat responseFormat = new ResponseFormat().of(favoritesService.getFavoritesByUser(users.getUser()), "success");
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }

    /**
     * 즐겨찾기 저장
     */
    @PostMapping("/beans")
    public ResponseEntity<?> doFavoritesByUser(@Valid @RequestBody FavoritesRequestDto favoritesRequestDto, @AuthenticationPrincipal UserDetailsImpl users) {

        String msg = favoritesService.doFavoritesByUser(favoritesRequestDto.getBean_id(),users.getUser());

        ResponseFormat responseFormat = new ResponseFormat().of(msg);
        return new ResponseEntity<>(responseFormat, HttpStatus.OK);

    }





}
