package com.jjss.civideo.global.config.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Set;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jjss.civideo.domain"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("CIVideo API Docs")
                .description("CIVideo API Docs")
                .version("1.0")
                .build();
    }

    private Set<String> getConsumeContentTypes() {
        return Set.of("application/json;charset=UTF-8", "application/x-www-form-urlencoded");
    }

    private Set<String> getProduceContentTypes() {
        return Set.of("application/json;charset=UTF-8");
    }

}
