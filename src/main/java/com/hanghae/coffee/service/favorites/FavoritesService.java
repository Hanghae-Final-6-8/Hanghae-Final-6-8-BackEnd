package com.hanghae.coffee.service.favorites;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.model.Beans;
import com.hanghae.coffee.model.Favorites;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.beans.BeansRepository;
import com.hanghae.coffee.repository.favorites.FavoritesRepository;
import java.util.List;
import java.util.Optional;
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

    public List<BeansListDto> getFavoritesByUser(Users users) {

        return favoritesRepository.getFavoritesByUser(users.getId());
    }

    @Transactional(readOnly = false)
    public void doFavoritesByUser(Long beanId, Users users) {

        Optional<Favorites> favorites = Optional.ofNullable(
            favoritesRepository.findByBeansIdAndUsersId(beanId,
                users.getId()));

        Beans beans = beansRepository.findById(beanId)
            .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST, "원두 정보가 없습니다."));

        favorites.ifPresentOrElse(f -> favoritesRepository.deleteById(f.getId()),
            () -> favoritesRepository.save(Favorites.createFavorites(users, beans))
        );

    }


}
