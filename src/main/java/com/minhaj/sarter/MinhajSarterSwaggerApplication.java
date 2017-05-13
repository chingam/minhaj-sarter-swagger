package com.minhaj.sarter;

import java.util.Calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ComponentScan({ "com.minhaj.sarter.controller", "com.minhaj.sarter.entity", "com.minhaj.sarter.service", "com.minhaj.sarter.config"})
@EnableSwagger2
@SpringBootApplication
public class MinhajSarterSwaggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinhajSarterSwaggerApplication.class, args);
	}
	
	@Bean
	public Docket empowerHubApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/user.*|/api.*")).build().pathMapping("/")
				.directModelSubstitute(Calendar.class, String.class);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Starter Api").description("For Distributors")
				.contact(new Contact("Minhajul Sarkar", "http://www.msr.com/", "polash.pbt@gmail.com")).version("0.0.1").build();
}
}
