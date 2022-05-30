package com.hanghae.coffee.service.file;

import com.hanghae.coffee.advice.RestException;
import com.hanghae.coffee.dto.beans.BeansDto;
import com.hanghae.coffee.dto.cafe.CafeDto;
import com.hanghae.coffee.model.Beans;
import com.hanghae.coffee.model.Cafe;
import com.hanghae.coffee.repository.beans.BeansRepository;
import com.hanghae.coffee.repository.cafe.CafeRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvFiletoDBService {

    private final ImageFileToS3Service imageFileToS3Service;
    private final BeansRepository beansRepository;
    private final CafeRepository cafeRepository;

    /**
     * @return
     */
    public List<CafeDto> readCafeCSV() {

        BufferedReader br = null;

        List<CafeDto> cafeDtoList = new LinkedList<>();
        File csv = new File("src/main/resources/db/cafe.csv");
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csv));
            while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                String[] lineArr = line.split(","); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.
                CafeDto cafeDto = new CafeDto();
                cafeDto.setCafeId(Long.valueOf(lineArr[0]));
                cafeDto.setCafeName(lineArr[1]);

                cafeDtoList.add(cafeDto);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close(); // 사용 후 BufferedReader를 닫아준다.


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cafeDtoList;

    }

    private List<BeansDto> readBeansCSV() {

        List<BeansDto> beansDtoList = new LinkedList<>();

        File csv = new File("src/main/resources/db/beans.csv");
        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csv));
            while ((line = br.readLine()) != null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                String[] lineArr = line.split("\\^"); // 파일의 한 줄을 ,로 나누어 배열에 저장 후 리스트로 변환한다.

                BeansDto beansDto = new BeansDto();
                beansDto.setBeanId(Long.valueOf(lineArr[0]));
                beansDto.setType(Integer.parseInt(lineArr[1]));
                beansDto.setAcidity(Integer.parseInt(lineArr[2]));
                beansDto.setSweetness(Integer.parseInt(lineArr[3]));
                beansDto.setBitter(Integer.parseInt(lineArr[4]));
                beansDto.setBody(Integer.parseInt(lineArr[5]));
                beansDto.setNutty(Integer.parseInt(lineArr[6]));
                beansDto.setFloral(Integer.parseInt(lineArr[7]));
                beansDto.setFruitFlavor(Integer.parseInt(lineArr[8]));
                beansDto.setNuttyFlavor(Integer.parseInt(lineArr[9]));
                beansDto.setCocoaFlavor(Integer.parseInt(lineArr[10]));
                beansDto.setCafeId(Long.valueOf(lineArr[11]));
                beansDto.setBeanName(lineArr[12]);
                log.info(lineArr[12]);
                beansDto.setDescription(lineArr[13]);

                beansDtoList.add(beansDto);
            }
        } catch (FileNotFoundException e) {
            throw new RestException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
        } catch (IOException e) {
            throw new RestException(HttpStatus.NOT_FOUND, "잘못된 파일입니다.");
        } finally {
            try {
                if (br != null) {
                    br.close(); // 사용 후 BufferedReader를 닫아준다.
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return beansDtoList;
    }

    @Transactional
    public void jdbcCafeInsert() throws IOException {

        List<CafeDto> cafeDtoList = readCafeCSV();

        for (CafeDto cafeDto : cafeDtoList) {

            String logoUrl = imageFileToS3Service.cafeLogoImageSave(cafeDto.getCafeId(),
                cafeDto.getCafeName() + "_logo");
            String bgUrl = imageFileToS3Service.cafeBackGroundImageSave(cafeDto.getCafeId(),
                cafeDto.getCafeName() + "_bg");

            Cafe cafe = Cafe.builder()
                .id(cafeDto.getCafeId())
                .cafeName(cafeDto.getCafeName())
                .cafeLogoImage(logoUrl)
                .cafeBackGroundImage(bgUrl)
                .build();

            cafeRepository.save(cafe);
        }

    }


    public void jdbcBeansInsert() throws IOException {

        List<BeansDto> beansDtoList = readBeansCSV();

        for (BeansDto beansDto : beansDtoList) {
            String imageUrl = imageFileToS3Service.beansImageSave(beansDto.getBeanId(),
                beansDto.getBeanName());

            beansDto.setBeanImage(imageUrl);
            Optional<Cafe> cafe = cafeRepository.findById(beansDto.getCafeId());

            Beans beans = Beans.builder()
                .beansDto(beansDto)
                .cafe(cafe.get())
                .build();

            beansRepository.save(beans);
        }

    }

}
