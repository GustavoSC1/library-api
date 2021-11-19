package com.gustavo.libraryapi.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.libraryapi.exception.BusinessException;
import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.repository.BookRepository;
import com.gustavo.libraryapi.service.impl.BookServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
	
	BookService service;
	
	@MockBean
	BookRepository repository;
	
	@BeforeEach
	public void setUp() {
		this.service = new BookServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		// Cenário
		Book book = createdValidBook();
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false);
		Mockito.when(repository.save(book)).thenReturn(Book.builder().id(1l).isbn("123").author("Fulano").title("As aventuras").build());
		
		// Execução
		Book savedBook = service.save(book);
		
		// Verificação
		Assertions.assertThat(savedBook.getId()).isNotNull();
		Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");
		Assertions.assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Fulano");		
	}
	
	@Test
	@DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn deplicado")
	public void shouldNotSaveABookWithDuplicatedISBN() {
		// Cenário
		Book book = createdValidBook();
		Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
		
		// Execução
		Throwable exception = Assertions.catchThrowable(() -> service.save(book));
		
		// Verificação
		Assertions.assertThat(exception)
			.isInstanceOf(BusinessException.class)
			.hasMessage("Isbn já cadastrado.");
		
		Mockito.verify(repository, Mockito.never()).save(book);
	}
	
	private Book createdValidBook() {
		return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
	}

}
