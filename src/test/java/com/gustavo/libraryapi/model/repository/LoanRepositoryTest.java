package com.gustavo.libraryapi.model.repository;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.entity.Loan;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {
	
	@Autowired
	private LoanRepository repository;
		
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	@DisplayName("Deve verificar se existe empréstimo não devolvido para o livro")
	public void existsByBookAndNotReturnedTest() {
		// Cenário
		Loan loan = createAndPersistLoan();
		
		// Execução
		boolean exists = repository.existsByBookAndNotReturned(loan.getBook());
		
		Assertions.assertThat(exists).isTrue();		
	}
	
	@Test
	@DisplayName("Deve buscar empréstimo pelo isbn do livro ou customer")
	public void findByBookIsbnOrCustomerTest() {
		Loan loan = createAndPersistLoan();
		
		Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));
	
		Assertions.assertThat(result.getContent()).hasSize(1);
		Assertions.assertThat(result.getContent()).contains(loan);
		Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
		Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
	}
	
	private Book createNewBook(String isbn) {
		return Book.builder().title("Aventuras").author("Fulano").isbn(isbn).build();
	}
	
	private Loan createAndPersistLoan() {
		Book book = createNewBook("123");
		entityManager.persist(book);
		
		Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(LocalDate.now()).build();
		entityManager.persist(loan);
		
		return loan;
	}

}
