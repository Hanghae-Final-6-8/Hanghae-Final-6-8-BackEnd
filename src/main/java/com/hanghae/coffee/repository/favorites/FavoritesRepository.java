package com.hanghae.coffee.repository.favorites;

import com.hanghae.coffee.model.Favorites;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritesRepository extends JpaRepository<Favorites, Long>,FavoritesCustomRepository {

    Optional<Favorites> findByBeansIdAndUsersId(Long beanId, Long userId);

}
