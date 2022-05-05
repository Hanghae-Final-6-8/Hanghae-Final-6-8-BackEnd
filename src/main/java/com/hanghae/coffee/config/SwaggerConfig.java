package com.hanghae.coffee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.hanghae.coffee")) // 현재 RequestMapping으로 할당된 모든 URL 리스트를 추출
            .paths(PathSelectors.ant("/api/**")) // 그중 /api/** 인 URL들만 필터링
            .build();
    }

//    @Bean
//    public Docket api() {
//        return new Docket(DocumentationType.OAS_30)
//            .useDefaultResponseMessages(false)
//            .select()
//            .apis(RequestHandlerSelectors.basePackage("com.example.springswagger.controller"))
//            .paths(PathSelectors.any())
//            .build()
//            .apiInfo(apiInfo());
//    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Practice Swagger")
            .description("practice swagger config")
            .version("1.0")
            .build();
    }
}
