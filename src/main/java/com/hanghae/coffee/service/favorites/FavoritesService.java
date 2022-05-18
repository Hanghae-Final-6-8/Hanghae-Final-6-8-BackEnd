package com.hanghae.coffee.service.favorites;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.beans.BeansListResponseDto;
import com.hanghae.coffee.dto.favorites.FavoritesResponseDto;
import com.hanghae.coffee.model.Beans;
import com.hanghae.coffee.model.Favorites;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.beans.BeansRepository;
import com.hanghae.coffee.repository.favorites.FavoritesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final BeansRepository beansRepository;

    public BeansListResponseDto getFavoritesByUser(Users users) {

        Long userId = users.getId();

        List<BeansListDto> beansList = favoritesRepository.getFavoritesByUser(userId);

        return BeansListResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(beansList)
            .build();
    }
    @Transactional(readOnly = false)
    public FavoritesResponseDto doFavoritesByUser(Long beanId, Users users) {

        Favorites favorites = favoritesRepository.findByBeansIdAndUsersId(beanId, users.getId())
            .orElse(null);

        if (favorites != null) {

            favoritesRepository.delete(favorites);

        } else {

            Beans beans = beansRepository.findById(beanId)
                .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, "원두 정보가 없습니다."));

            favorites = Favorites.createFavorites(users, beans);

            favoritesRepository.save(favorites);

        }

        return FavoritesResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .build();

    }


}
