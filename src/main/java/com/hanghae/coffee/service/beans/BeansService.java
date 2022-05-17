package com.hanghae.coffee.service.beans;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.beans.BeansDto;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.beans.BeansListResponseDto;
import com.hanghae.coffee.dto.beans.BeansResponseDto;
import com.hanghae.coffee.repository.beans.BeansRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BeansService {

    private final BeansRepository beansRepository;

    public BeansResponseDto getBeansByBeanId(Long id) {

        BeansDto beansDto = beansRepository.getBeansByBeanId(id).orElse(null);

        if (beansDto == null) {
            throw new RestException(HttpStatus.BAD_REQUEST, "원두 정보가 없습니다");
        }

        return BeansResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(beansDto)
            .build();

    }

    public BeansListResponseDto getBeansList() {

        List<BeansListDto> beansList = beansRepository.getBeansList();

        return BeansListResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(beansList)
            .build();

    }

    public BeansListResponseDto getBeansListByKeyword(String keyword) {

        List<BeansListDto> beansList = beansRepository.getBeansListByKeyword(keyword);

        return BeansListResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(beansList)
            .build();

    }


}
