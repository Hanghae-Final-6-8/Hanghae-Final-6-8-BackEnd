package com.hanghae.coffee.repository.beans;

import com.hanghae.coffee.dto.beans.BeansDto;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.taste.TasteRequestDto;
import java.util.List;
import java.util.Optional;

public interface BeansCustomRepository {

    Optional<BeansDto> getBeansByBeanId(Long beanId);

    List<BeansListDto> getBeansList();

    List<BeansDto> getBeansByBeanTaste(TasteRequestDto tasteRequestDto);

    List<BeansListDto> getBeansListByKeyword(String keyword);

}
