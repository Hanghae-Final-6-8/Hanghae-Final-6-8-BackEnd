package com.hanghae.coffee.service.taste;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.beans.BeansDto;
import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.taste.TasteDto;
import com.hanghae.coffee.dto.taste.TasteRequestDto;
import com.hanghae.coffee.model.Beans;
import com.hanghae.coffee.model.Taste;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.beans.BeansRepository;
import com.hanghae.coffee.repository.taste.TasteRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TasteService {

    private final TasteRepository tasteRepository;
    private final BeansRepository beansRepository;

    public TasteDto findTasteByUser(Users users) {

        return tasteRepository.findTasteByUser(users.getId()).orElse(null);
    }

    @Transactional(readOnly = false)
    public BeansDto doTasteByUser(Users users,TasteRequestDto tasteRequestDto){

        List<BeansDto> beansDtoList = beansRepository.getBeansByBeanTaste(tasteRequestDto);

        Beans beans;

        //임시.. 알고리즘 적용해야함
        if(beansDtoList.size() == 0){
            beans = beansRepository.findById(6L)
                .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST,"원두 정보가 없습니다."));
        }else {
            beans = beansRepository.findById(beansDtoList.get(0).getBeanId())
                .orElseThrow(() -> new RestException(HttpStatus.BAD_REQUEST,"원두 정보가 없습니다."));
        }

        Taste taste = tasteRepository.findByUsersId(users.getId()).orElse(null);

        if(taste != null){
            taste.updateTaste(users, beans);

        }else {
            taste = Taste.createTaste(users, beans);
            tasteRepository.save(taste);
        }

        BeansDto beansDto = beansRepository.getBeansByBeanId(beans.getId())
            .orElseThrow(() -> new RestException(HttpStatus.OK,"원두 정보가 없습니다."));

        return beansDto;
    }

    public List<BeansListDto> findTasteListByUserTaste(Users users){

        Long userId = users.getId();

        TasteDto tasteDto = tasteRepository.findTasteByUser(userId).orElse(null);

        //임시 쿼리
        List<BeansListDto> beansListDto = tasteRepository.findTasteListByUserTaste(tasteDto);

        return beansListDto;
    }

}
