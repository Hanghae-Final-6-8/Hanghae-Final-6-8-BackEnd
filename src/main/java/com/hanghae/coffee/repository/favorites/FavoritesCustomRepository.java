package com.hanghae.coffee.repository.favorites;

import com.hanghae.coffee.dto.beans.BeansListDto;
import java.util.List;

public interface FavoritesCustomRepository {

    List<BeansListDto> getFavoritesByUser(Long userId);

}
