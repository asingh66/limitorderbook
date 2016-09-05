package com.capone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "com.capone" })
public class LimitOrderBookApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LimitOrderBookApiApplication.class, args);
	}
	
	@Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("orders")
                .apiInfo(apiInfo())
                .select()
                //.paths(PathSelectors.any())
                .paths(PathSelectors.ant("/api/orders*/**"))
                .build();
    }
     
	@Bean
	public TestRestTemplate restTemplate() {
	    return new TestRestTemplate();
	}
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Limit Order Book Rest API")
                .description("Limit Order Book Rest API. Use the API to place orders")
                .version("2.0")
                .build();
    }
	

}