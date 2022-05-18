package com.hanghae.coffee;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConfigurationPropertiesScan
@SpringBootApplication
@EnableJpaAuditing // 시간 자동 변경이 가능하도록 합니다.
public class FinalProjectApplication {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.default", "prod");
		SpringApplication.run(FinalProjectApplication.class, args);

	}

	@PostConstruct
	public void setup(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
