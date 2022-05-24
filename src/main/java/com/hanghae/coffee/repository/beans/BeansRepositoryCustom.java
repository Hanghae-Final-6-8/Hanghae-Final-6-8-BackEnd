package com.hanghae.coffee.repository.beans;

import com.hanghae.coffee.dto.beans.BeansDto;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.taste.TasteRequestDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeansRepositoryCustom {

    Optional<BeansDto> getBeansByBeanId(Long userId, Long beanId);

    Page<BeansListDto> getBeansList(String type, Pageable pageable);

    List<BeansDto> getBeansByBeanTaste(TasteRequestDto tasteRequestDto);

    List<BeansListDto> getBeansListByKeyword(String keyword);

}
