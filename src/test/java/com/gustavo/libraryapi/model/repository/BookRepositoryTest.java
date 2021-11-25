package com.gustavo.libraryapi.model.repository;

import java.util.Optional;

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
		Book book = createNewBook(isbn);
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
	
	@Test
	@DisplayName("Deve obter um livro por id")
	public void findByIdTest() {
		// Cenario
		Book book = createNewBook("123");
		entityManager.persist(book);
		
		// Execução
		Optional<Book> foundBook = repository.findById(book.getId());
		
		// Verificação
		Assertions.assertThat(foundBook.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		Book book = createNewBook("123");
		
		Book savedBook = repository.save(book);
		
		Assertions.assertThat(savedBook.getId()).isNotNull();
	}
	
	public void deleteBookTest() {
		
		Book book = createNewBook("123");
		entityManager.persist(book);
		
		Book foundBook = entityManager.find(Book.class, book.getId());
		
		repository.delete(foundBook);
		
		Book deletedBook = entityManager.find(Book.class, book.getId());
		
		Assertions.assertThat(deletedBook).isNull();
	}
	
	private Book createNewBook(String isbn) {
		return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
	}
	
}
