package com.hanghae.coffee.service.cafe;

import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.beans.BeansListResponseDto;
import com.hanghae.coffee.dto.cafe.CafeDto;
import com.hanghae.coffee.dto.cafe.CafeResponseDto;
import com.hanghae.coffee.model.Beans;
import com.hanghae.coffee.model.Cafe;
import com.hanghae.coffee.repository.cafe.CafeRepository;
import com.hanghae.coffee.repository.beans.BeansRepository;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final CafeRepository cafeRepository;
    private final BeansRepository beansRepository;

    public CafeResponseDto getCafeList() {

        List<CafeDto> cafeDtoList = new LinkedList<>();

        List<Cafe> cafeList = cafeRepository.findAll();

        for (Cafe cafe : cafeList) {
            CafeDto cafeDto = new CafeDto();
            cafeDto.setId(cafe.getId());
            cafeDto.setCafeName(cafe.getCafeName());

            cafeDtoList.add(cafeDto);

        }

        return CafeResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(cafeDtoList)
            .build();

    }

    public BeansListResponseDto getBeansListByCafeId(Long cafeId) {

        List<BeansListDto> resultDto = new LinkedList<>();

        List<Beans> beansList = beansRepository.findAllByCafeId(cafeId);

        for (Beans beans : beansList) {
            BeansListDto beansListDto = new BeansListDto();
            beansListDto.setBeanId(beans.getId());
            beansListDto.setBeanName(beans.getBeanName());
            beansListDto.setType(beans.getType());
            beansListDto.setDescription(beans.getDescription());

            resultDto.add(beansListDto);

        }

        return BeansListResponseDto
            .builder()
            .status(HttpStatus.OK)
            .msg("success")
            .data(resultDto)
            .build();

    }

}
