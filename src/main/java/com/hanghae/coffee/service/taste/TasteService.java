package com.hanghae.coffee.service.taste;

import com.hanghae.coffee.dto.beans.BeansListDto;
import com.hanghae.coffee.dto.taste.TasteDto;
import com.hanghae.coffee.dto.taste.TasteRequestDto;
import com.hanghae.coffee.model.Beans;
import com.hanghae.coffee.model.Taste;
import com.hanghae.coffee.model.Users;
import com.hanghae.coffee.repository.beans.BeansRepository;
import com.hanghae.coffee.repository.taste.TasteRepository;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TasteService {

    private final TasteRepository tasteRepository;
    private final BeansRepository beansRepository;

    public TasteDto findTasteByUser(Users users) {

        return tasteRepository.findTasteByUser(users.getId()).orElse(null);
    }

    @Transactional(readOnly = false)
    public void doTasteByUser(Users users, TasteRequestDto tasteRequestDto) {

        List<Beans> originBeansList = beansRepository.findAll();

        // 1차
        List<Beans> beansFlavor = getBeansFlavor(originBeansList, tasteRequestDto, "1");

        if (beansFlavor.size() < 1) {
            beansFlavor = getBeansFlavor(originBeansList, tasteRequestDto, "2");
        }

        Optional<Beans> beans = getBeansTasteRepeat(beansFlavor, tasteRequestDto);

        // 2차
        if (beans.isEmpty()) {
            beansFlavor = getBeansFlavor(originBeansList, tasteRequestDto, "2");

            beans = getBeansTasteRepeat(beansFlavor, tasteRequestDto);
        }

        Taste taste = tasteRepository.findByUsersId(users.getId()).orElse(null);

        if (taste != null) {
            taste.updateTaste(users, beans.get());

        } else {
            taste = Taste.createTaste(users, beans.get());
            tasteRepository.save(taste);
        }

    }

    private List<Beans> getBeansFlavor(List<Beans> beans, TasteRequestDto tasteRequestDto,
        String type) {

        if (type.equals("1")) {

            return beans.stream().filter(
                b ->
                    b.getFloral() == tasteRequestDto.getFloral() &&
                        b.getFruitFlavor() == tasteRequestDto.getFruit_flavor() &&
                        b.getCocoaFlavor() == tasteRequestDto.getCocoa_flavor() &&
                        b.getNuttyFlavor() == tasteRequestDto.getNutty_flavor()
            ).toList();

        } else if (type.equals("2")) {

            return beans.stream().filter(
                b ->
                    (tasteRequestDto.getFloral() == 1 ||
                        b.getFloral() == tasteRequestDto.getFloral()) &&
                        (tasteRequestDto.getFruit_flavor() == 1 ||
                            b.getFruitFlavor() == tasteRequestDto.getFruit_flavor()) &&
                        (tasteRequestDto.getCocoa_flavor() == 1 ||
                            b.getCocoaFlavor() == tasteRequestDto.getCocoa_flavor()) &&
                        (tasteRequestDto.getNutty_flavor() == 1 ||
                            b.getNuttyFlavor() == tasteRequestDto.getNutty_flavor())
            ).toList();

        }

        return beans;

    }

    //사용자 취향 기준 원두 선정하기
    private Optional<Beans> getBeansTaste(List<Beans> beans, TasteRequestDto tasteRequestDto,
        String type) {
        List<Beans> beansList = new ArrayList<>();
        if (type.equals("1")) {
            beansList = beans.stream().filter(
                b ->
                    b.getAcidity() == tasteRequestDto.getAcidity() &&
                        b.getBitter() == tasteRequestDto.getBitter() &&
                        b.getSweetness() == tasteRequestDto.getSweetness() &&
                        b.getBody() == tasteRequestDto.getBody() &&
                        b.getNutty() == tasteRequestDto.getNutty()
            ).toList();

        } else if (type.equals("2")) {

            beansList = beans.stream().filter(
                b ->
                    (getAbs(b.getAcidity(), tasteRequestDto.getAcidity()) < 2 ||
                        b.getAcidity() == tasteRequestDto.getAcidity()) &&
                        (getAbs(b.getBitter(), tasteRequestDto.getBitter()) < 2 ||
                            b.getBitter() == tasteRequestDto.getBitter()) &&
                        (getAbs(b.getSweetness(), tasteRequestDto.getSweetness()) < 2 ||
                            b.getSweetness() == tasteRequestDto.getSweetness()) &&
                        (getAbs(b.getBody(), tasteRequestDto.getBody()) < 2 ||
                            b.getBody() == tasteRequestDto.getBody()) &&
                        (getAbs(b.getNutty(), tasteRequestDto.getNutty()) < 2 ||
                            b.getNutty() == tasteRequestDto.getNutty())

            ).toList();

        }

        beansList.stream().map(b -> b.getBeanName()).forEach(System.out::println);
        System.out.println("--------------------------------------------------------------------");

        if (beansList.size() > 0) {
            Random rand = new Random();
            return Optional.ofNullable(beansList.get(rand.nextInt(beansList.size())));
        }
        return Optional.empty();
    }

    private Optional<Beans> getBeansTasteRepeat(List<Beans> beansFlavor,
        TasteRequestDto tasteRequestDto) {

        Optional<Beans> beans = getBeansTaste(beansFlavor, tasteRequestDto, "1");

        if (beans.isEmpty()) {
            beans = getBeansTaste(beansFlavor, tasteRequestDto, "2");
        }

        return beans;
    }

    private int getAbs(int x, int y) {
        return Math.abs(x - y);
    }


    public List<BeansListDto> findTasteListByUserTaste(Users users) {

        List<BeansListDto> beansListDtoList = new LinkedList<>();

        Long userId = users.getId();

        TasteDto tasteDto = tasteRepository.findTasteByUser(userId).orElse(null);

        List<Beans> originBeansList = beansRepository.findAll();

        TasteRequestDto tasteRequestDto = new TasteRequestDto();
        tasteRequestDto.setAcidity(tasteDto.getAcidity());
        tasteRequestDto.setSweetness(tasteDto.getSweetness());
        tasteRequestDto.setBitter(tasteDto.getBitter());
        tasteRequestDto.setBody(tasteDto.getBody());
        tasteRequestDto.setNutty(tasteDto.getNutty());
        tasteRequestDto.setFloral(tasteDto.getFloral());
        tasteRequestDto.setFruit_flavor(tasteDto.getFruitFlavor());
        tasteRequestDto.setCocoa_flavor(tasteDto.getCocoaFlavor());
        tasteRequestDto.setNutty_flavor(tasteDto.getNuttyFlavor());

        originBeansList.stream()
            .filter(beans -> beans.getId().equals(tasteDto.getBeanId()))
            .collect(Collectors.toList())
            .forEach(li -> originBeansList.remove(li));

        List<Beans> beans = getBeansFlavor(originBeansList, tasteRequestDto, "2");

        beans = beans.stream().filter(
            b ->
                (getAbs(b.getAcidity(), tasteRequestDto.getAcidity()) < 2 ||
                    b.getAcidity() == tasteRequestDto.getAcidity()) &&
                    (getAbs(b.getBitter(), tasteRequestDto.getBitter()) < 2 ||
                        b.getBitter() == tasteRequestDto.getBitter()) &&
                    (getAbs(b.getSweetness(), tasteRequestDto.getSweetness()) < 2 ||
                        b.getSweetness() == tasteRequestDto.getSweetness()) &&
                    (getAbs(b.getBody(), tasteRequestDto.getBody()) < 2 ||
                        b.getBody() == tasteRequestDto.getBody()) &&
                    (getAbs(b.getNutty(), tasteRequestDto.getNutty()) < 2 ||
                        b.getNutty() == tasteRequestDto.getNutty())

        ).toList();

        if (beans.size() > 4) {
            beans = getRandomElement(beans, 4);
        }

        for (Beans b : beans) {
            BeansListDto beansListDto = new BeansListDto();
            beansListDto.setBeanId(b.getId());
            beansListDto.setBeanName(b.getBeanName());
            beansListDto.setType(b.getType());
            beansListDto.setDescription(b.getDescription());
            beansListDto.setBeanImage(b.getBeanImage());

            beansListDtoList.add(beansListDto);
        }


        return beansListDtoList;
    }

    private List<Beans> getRandomElement(List<Beans> list, int totalItems) {

        List<Beans> beans = new LinkedList<>();

        Random r = new Random();

        for (int i = 0; i < totalItems; i++) {

            Beans e = list.get(r.nextInt(list.size()));
            beans.add(e);
            log.info(String.valueOf(e.getBeanName()));
            list = list.stream().filter(b -> !b.getId().equals(e.getId())).toList();

        }

        return beans;

    }

}
