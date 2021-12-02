package com.gustavo.libraryapi.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gustavo.libraryapi.api.dto.LoanFilterDTO;
import com.gustavo.libraryapi.exception.BusinessException;
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
		
		Mockito.when(repository.existsByBookAndNotReturned(book)).thenReturn(false);
		Mockito.when(repository.save(savingLoan)).thenReturn(savedLoan);
		
		Loan loan = service.save(savingLoan);
		
		Assertions.assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		Assertions.assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		Assertions.assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		Assertions.assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
	}
	
	@Test
	@DisplayName("Deve lançar erro de negócio ao salvar um empréstimo com livro já emprestado")
	public void loanedBookSaveTest() {
		Book book = Book.builder().id(1l).build();
		String customer = "Fulano";
		
		Loan savingLoan = 
				Loan.builder()
				.book(book)
				.customer(customer)
				.loanDate(LocalDate.now())
				.build();
		
		Mockito.when(repository.existsByBookAndNotReturned(book)).thenReturn(true);
		
		Throwable exception = Assertions.catchThrowable(() -> service.save(savingLoan));
		
		Assertions.assertThat(exception)
							.isInstanceOf(BusinessException.class)
							.hasMessage("Book already loaned");
		
		Mockito.verify(repository, Mockito.never()).save(savingLoan);
	}
	
	@Test
	@DisplayName("Deve obter as informações de um empréstimo pelo ID")
	public void getLoanDataisTest() {
		// Cenário
		Long id = 1l;
		
		Loan loan = createLoan();
		loan.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(loan));
		
		// Execução
		Optional<Loan> result = service.getById(id);
		
		// Verificação
		Assertions.assertThat(result.isPresent()).isTrue();
		Assertions.assertThat(result.get().getId()).isEqualTo(loan.getId());
		Assertions.assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
		Assertions.assertThat(result.get().getBook()).isEqualTo(loan.getBook());
		Assertions.assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());
		
		Mockito.verify(repository).findById(id);
	}
	
	@Test
	@DisplayName("Deve atualizar um empréstimo")
	public void updateLOanTest() {
		Loan loan = createLoan();
		loan.setId(1l);
		loan.setReturned(true);
		
		Mockito.when(repository.save(loan)).thenReturn(loan);
		
		Loan updatedLoan = service.update(loan);
		
		Assertions.assertThat(updatedLoan.getReturned()).isTrue();
		Mockito.verify(repository).save(loan);
	}
	
	@Test
	@DisplayName("Deve filtrar empréstimos pelas propriedades")
	public void findLoanTest() {
		// Cenário
		LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("Fulano").isbn("321").build();
						
		Loan loan = createLoan();
		loan.setId(1l);
		
		// Pageable: interface abstrata para informações de paginação
		// PageRequest: é uma implementação da interface Pageable
		PageRequest pageRequest = PageRequest.of(0, 10);//(pagina, quantidade maximo de elementos na pagina)
		
		List<Loan> lista = Arrays.asList(loan);
		
		// PageImpl: Cria uma página utilziando uma lista 
		Page<Loan> page = new PageImpl<Loan>(lista, pageRequest, lista.size()); //(conteúdo desta página, informações de paginação, quantidade total de itens disponíveis)
		
		Mockito.when(repository.findByBookIsbnOrCustomer(
				Mockito.anyString(), 
				Mockito.anyString(),
				Mockito.any(PageRequest.class))
				)
				.thenReturn(page);
		
		// Execução
		Page<Loan> result = service.find(loanFilterDTO, pageRequest);
		
		// Verificação
		Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
		Assertions.assertThat(result.getContent()).isEqualTo(lista);
		Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
		Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
	}
	
	private Loan createLoan() {
		Book book = Book.builder().id(1l).build();
		String customer = "Fulano";
		
		Loan savingLoan = 
				Loan.builder()
				.book(book)
				.customer(customer)
				.loanDate(LocalDate.now())
				.build();
		
		return savingLoan;
	}
	
}
