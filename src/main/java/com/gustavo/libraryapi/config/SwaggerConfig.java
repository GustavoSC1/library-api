package com.gustavo.libraryapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.gustavo.libraryapi.api.resource"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	
	// É um objeto que terá as informações da API Ex: título, descrição, versão)
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
					.title("Library API")
					.description("API do Projeto de controle de aluguel de livros")
					.version("1.0")
					.contact(contact())
					.build();
	}
	
	// É um objeto que terá as informações do desenvolvedor
	private Contact contact() {
		return new Contact("Gustavo da Silva Cruz", 
						   "https://github.com/GustavoSC1", 
						   "gu.cruz17@hotmail.com");
	}

}
