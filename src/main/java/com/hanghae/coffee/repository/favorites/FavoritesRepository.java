package com.hanghae.coffee.repository.favorites;

import com.hanghae.coffee.model.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Long>,
    FavoritesRepositoryCustom {

    Favorites findByBeansIdAndUsersId(Long beanId, Long userId);

}
