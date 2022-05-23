package com.hanghae.coffee.repository.favorites;

import com.hanghae.coffee.dto.beans.BeansListDto;
import java.util.List;

public interface FavoritesRepositoryCustom {

    List<BeansListDto> getFavoritesByUser(Long userId);

}
