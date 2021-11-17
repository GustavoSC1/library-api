package com.gustavo.libraryapi.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.libraryapi.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
	
	BookService service;
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		// Cenário
		Book book = Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
		
		// Execução
		Book savedBook = service.save(book);
		
		// Verificação
		Assertions.assertThat(savedBook.getId()).isNotNull();
		Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");
		Assertions.assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		
	}

}
