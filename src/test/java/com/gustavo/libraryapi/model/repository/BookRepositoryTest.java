package com.gustavo.libraryapi.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.libraryapi.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
// Indica que serão realizados testes com JPA, ou seja, será criada uma instância do banco de dados em memória 
// apenas para executar os testes da classe e, no final dos testes, ele vai apagar tudo.
@DataJpaTest
public class BookRepositoryTest {
	
	@Autowired 
	// EntityManager é utilizado dentro das implementações do JpaRepository para executar as 
	// operações da base de dados	
	TestEntityManager entityManager;
	
	@Autowired
	BookRepository repository;
	
	@Test
	@DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
	public void returnTrueWhenIsbnExists() {
		// Cenario
		String isbn = "123";
		Book book = Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
		entityManager.persist(book);
		
		// Execução
		boolean exists = repository.existsByIsbn(isbn);
		
		// Verificação
		Assertions.assertThat(exists).isTrue();
	}
	
	@Test
	@DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado")
	public void returnFalseWhenIsbnDoesntExists() {
		// Cenario
		String isbn = "123";
		
		// Execução
		boolean exists = repository.existsByIsbn(isbn);
		
		// Verificação
		Assertions.assertThat(exists).isFalse();
	}
	
}
