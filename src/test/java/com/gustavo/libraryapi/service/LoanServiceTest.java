package com.gustavo.libraryapi.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.entity.Loan;
import com.gustavo.libraryapi.model.repository.LoanRepository;
import com.gustavo.libraryapi.service.impl.LoanServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {
	
	LoanService service;
	
	@MockBean
	LoanRepository repository;
	
	@BeforeEach
	public void setUp() {
		this.service = new LoanServiceImpl(repository);
	}
	
	@Test
	@DisplayName("Deve salvar um empréstimo")
	public void saveLoanTest() {
		Book book = Book.builder().id(1l).build();
		String customer = "Fulano";
		
		Loan savingLoan = 
				Loan.builder()
				.book(book)
				.customer(customer)
				.loanDate(LocalDate.now())
				.build();
		
		Loan savedLoan = Loan.builder()
				.id(1l)
				.loanDate(LocalDate.now())
				.customer(customer)
				.book(book).build();
		
		Mockito.when(repository.save(savingLoan)).thenReturn(savedLoan);
		
		Loan loan = service.save(savingLoan);
		
		Assertions.assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		Assertions.assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		Assertions.assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		Assertions.assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
	}
	
}
