package com.hanghae.coffee.repository.taste;

import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.taste.TasteDto;
import java.util.List;
import java.util.Optional;

public interface TasteCustomRepository {

    Optional<TasteDto> findTasteByUser(Long userId);

    List<BeansListDto> findTasteListByUserTaste(TasteDto tasteDto);

}
