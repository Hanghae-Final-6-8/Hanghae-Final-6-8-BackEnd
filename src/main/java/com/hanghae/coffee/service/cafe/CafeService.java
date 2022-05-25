package com.hanghae.coffee.service.cafe;

import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.cafe.CafeDto;
import com.hanghae.coffee.model.Beans;
import com.hanghae.coffee.model.Cafe;
import com.hanghae.coffee.repository.beans.BeansRepository;
import com.hanghae.coffee.repository.cafe.CafeRepository;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final CafeRepository cafeRepository;
    private final BeansRepository beansRepository;

    public List<CafeDto> getCafeList() {

        List<CafeDto> cafeDtoList = new LinkedList<>();

        List<Cafe> cafeList = cafeRepository.findAll();

        for (Cafe cafe : cafeList) {
            CafeDto cafeDto = new CafeDto();
            cafeDto.setCafeId(cafe.getId());
            cafeDto.setCafeName(cafe.getCafeName());

            cafeDtoList.add(cafeDto);

        }

        return cafeDtoList;

    }

    public List<BeansListDto> getBeansListByCafeId(Long cafeId) {

        List<BeansListDto> resultDto = new LinkedList<>();

        List<Beans> beansList = beansRepository.findAllByCafeId(cafeId);

        for (Beans beans : beansList) {
            BeansListDto beansListDto = new BeansListDto();
            beansListDto.setBeanId(beans.getId());
            beansListDto.setBeanName(beans.getBeanName());
            beansListDto.setType(beans.getType());
            beansListDto.setBeanImage(beans.getBeanImage());
            beansListDto.setDescription(beans.getDescription());

            resultDto.add(beansListDto);

        }

        return resultDto;

    }

    public List<CafeDto> getBeansTotalByCafe() {
        return cafeRepository.getBeansTotalByCafe();
    }

}
