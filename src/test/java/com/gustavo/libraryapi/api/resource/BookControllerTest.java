package com.gustavo.libraryapi.api.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

// Interface de extensão definida pelo JUnit 5 por meio da qual os recursos do Spring Boot podem se integrar ao teste JUnit.
@ExtendWith(SpringExtension.class)
//Será executado com o ambiente de testes
@ActiveProfiles("test")
// É realmente um teste de unidade do seu controlador e vai apenas varrer o controlador que você definiu e a 
//infraestrutura MVC. Se ele tiver dependência, você terá que fornecê-los você mesmo.
@WebMvcTest
// Annotations que injeta o MockMvc no contexto da aplicação.
@AutoConfigureMockMvc
public class BookControllerTest {
	
	// MockMvc fornece suporte para teste Spring MVC. Ele será responsável por invocar e testar o retorno das requisições.
	@Autowired
	MockMvc mvc;
	
	@Test
	@DisplayName("Deve criar um livro com sucesso.")
	public void createBookTest() {
		
	}
	
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do livro.")
	public void createInvalidBookTest() {
		
	}
}
