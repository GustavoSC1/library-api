package com.gustavo.libraryapi.api.resource;

import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.libraryapi.api.dto.BookDTO;
import com.gustavo.libraryapi.exception.BusinessException;
import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.service.BookService;
import com.gustavo.libraryapi.service.LoanService;


// Interface de extensão definida pelo JUnit 5 por meio da qual os recursos do Spring Boot podem se integrar ao teste JUnit.
@ExtendWith(SpringExtension.class)
//Será executado com o ambiente de testes
@ActiveProfiles("test")
// É realmente um teste de unidade do seu controlador e vai apenas varrer o controlador que você definiu e a 
//infraestrutura MVC. Se ele tiver dependência, você terá que fornecê-los você mesmo.
@WebMvcTest(controllers = BookController.class)// Vai usar no teste apenas o controlador Book
// Annotations que injeta o MockMvc no contexto da aplicação.
@AutoConfigureMockMvc
public class BookControllerTest {
	
	static String BOOK_API = "/api/books";
	
	// MockMvc fornece suporte para teste Spring MVC. Ele será responsável por invocar e testar o retorno das requisições.
	@Autowired
	MockMvc mvc;
	
	// Anotação utilizada pelo Spring para criar um Mock e adicionar no contexto de injeção de dependências
	@MockBean
	BookService service;
	
	@MockBean
	LoanService loanService;
	
	@Test
	@DisplayName("Deve criar um livro com sucesso.")
	public void createBookTest() throws Exception {
		
		BookDTO dto = createNewBook();
		Book savedBook = Book.builder().id(10l).author("Artur").title("As aventuras").isbn("001").build();
		
		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
		// Gera um JSON a partir de um objeto Java e retorna o JSON gerado como uma string
		String json = new ObjectMapper().writeValueAsString(dto);
		
		// Serve para definir uma requisição
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
													.post(BOOK_API)
													.contentType(MediaType.APPLICATION_JSON)
													.accept(MediaType.APPLICATION_JSON)
													.content(json);
		mvc
			.perform(request)
			.andExpect( MockMvcResultMatchers.status().isCreated() )
			.andExpect( MockMvcResultMatchers.jsonPath("id").value(10l) )
			.andExpect( MockMvcResultMatchers.jsonPath("title").value(dto.getTitle()) )
			.andExpect( MockMvcResultMatchers.jsonPath("author").value(dto.getAuthor()) )
			.andExpect( MockMvcResultMatchers.jsonPath("isbn").value(dto.getIsbn()) );
			
	}
		
	@Test
	@DisplayName("Deve lançar erro de validação quando não houver dados suficientes para criação do livro.")
	public void createInvalidBookTest() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(new BookDTO());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
			.andExpect( MockMvcResultMatchers.status().isBadRequest() )
			.andExpect( MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)) );
		
	}
	
	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já utilizado por outro.")
	public void createBookWithDuplicatedIsbn() throws Exception {
		
		BookDTO dto = createNewBook();		
		String json = new ObjectMapper().writeValueAsString(dto);
		String mensagemErro = "Isbn já cadastrado."; 
		BDDMockito.given(service.save(Mockito.any(Book.class))).willThrow(new BusinessException(mensagemErro));
				
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(mensagemErro));
		
	}
	
	@Test
	@DisplayName("Deve obter informações de um livro.")
	public void getBookDetailsTest() throws Exception {
		// Cenário
		Long id = 1l;
		Book book = Book.builder()
						.id(id).title(createNewBook().getTitle())
						.author(createNewBook().getAuthor())
						.isbn(createNewBook().getIsbn())
						.build();
		BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));
		
		// Execução
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(BOOK_API.concat("/" + id))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect( MockMvcResultMatchers.jsonPath("id").value(id) )
			.andExpect( MockMvcResultMatchers.jsonPath("title").value(createNewBook().getTitle()) )
			.andExpect( MockMvcResultMatchers.jsonPath("author").value(createNewBook().getAuthor()) )
			.andExpect( MockMvcResultMatchers.jsonPath("isbn").value(createNewBook().getIsbn()) );
	}
	
	@Test
	@DisplayName("Deve retornar resource not found quando o livro procurado não existir")
	public void bookNotFoundTest() throws Exception {
		
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(BOOK_API.concat("/" + 1))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc
		.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNotFound());
		
	}
	
	@Test
	@DisplayName("Deve deletar um livro")
	public void deleteBookTest() throws Exception {
		
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1l).build()));
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(BOOK_API.concat("/" + 1));
		
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
	
	@Test
	@DisplayName("Deve retornar resource not found quando não encontrar o livro para deletar")
	public void deleteInexistentBookTest() throws Exception {
		
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(BOOK_API.concat("/" + 1));
		
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	@DisplayName("Deve atualizar um livro")
	public void updateBookTest() throws Exception {
		Long id = 1l;
		String json = new ObjectMapper().writeValueAsString(createNewBook());
		
		Book updatingBook = Book.builder().id(1l).title("some title").author("some author").isbn("321").build();
		BDDMockito.given(service.getById(id)).willReturn(Optional.of(updatingBook));
		
		Book updateBook = Book.builder().id(id).author("Artur").title("As aventuras").isbn("321").build();
		BDDMockito.given(service.update(updatingBook)).willReturn(updateBook);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(BOOK_API.concat("/" + 1))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect( MockMvcResultMatchers.jsonPath("id").value(id) )
			.andExpect( MockMvcResultMatchers.jsonPath("title").value(createNewBook().getTitle()) )
			.andExpect( MockMvcResultMatchers.jsonPath("author").value(createNewBook().getAuthor()) )
			.andExpect( MockMvcResultMatchers.jsonPath("isbn").value("321"));
		
	}
	
	@Test
	@DisplayName("Deve retornar 404 ao tentar atualizar um livro inexistente")
	public void updateInexistentBookTest() throws Exception {
		
		String json = new ObjectMapper().writeValueAsString(createNewBook());

		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(BOOK_API.concat("/" + 1))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	@DisplayName("Deve filtrar livros")
	public void FindBooksTest() throws Exception {
		
		Long id = 1l;
		
		Book book = Book.builder().id(id).title(createNewBook().getTitle()).author(createNewBook().getAuthor()).isbn(createNewBook().getIsbn()).build();
		
		BDDMockito.given(service.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
					.willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1));
		
		String queryString = String.format("?title=%s&author=%s&page=0&size=100",
				book.getTitle(), book.getAuthor());
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat(queryString))
		.accept(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(100))
			.andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));
		
	}
		
	private BookDTO createNewBook() {
		return BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
	}
	
}
