package com.hanghae.coffee.repository.taste;

import com.hanghae.coffee.dto.taste.TasteDto;
import java.util.Optional;

public interface TasteRepositoryCustom {

    Optional<TasteDto> findTasteByUser(Long userId);

}
