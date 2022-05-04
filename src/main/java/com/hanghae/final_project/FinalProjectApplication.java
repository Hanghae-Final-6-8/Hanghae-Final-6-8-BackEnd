package com.hanghae.final_project;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 시간 자동 변경이 가능하도록 합니다.
public class FinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}

	@PostConstruct
	public void setup(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}