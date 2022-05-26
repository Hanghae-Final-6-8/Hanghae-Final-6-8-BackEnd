package com.hanghae.coffee.service.beans;

import com.hanghae.coffee.advice.ErrorCode;
import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.beans.BeansDto;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.repository.beans.BeansRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BeansService {

    private final BeansRepository beansRepository;

    public BeansDto getBeansByBeanId(Long userId, Long beanId) {

        return beansRepository.getBeansByBeanId(userId, beanId).orElseThrow(
            () -> new RestException(ErrorCode.NOT_FOUND_BEANS)
        );

    }

    public Page<BeansListDto> getBeansList(String type, Pageable pageable) {

        return beansRepository.getBeansList(type, pageable).map(BeansListDto::new);
    }

    public List<BeansListDto> getBeansListByKeyword(String keyword) {

        return beansRepository.getBeansListByKeyword(keyword);

    }
}

