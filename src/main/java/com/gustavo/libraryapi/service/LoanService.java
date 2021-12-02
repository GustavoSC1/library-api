package com.gustavo.libraryapi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gustavo.libraryapi.api.dto.LoanFilterDTO;
import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.entity.Loan;

public interface LoanService {
	
	Loan save(Loan loan);

	Optional<Loan> getById(Long id);

	Loan update(Loan loan);

	Page<Loan> find(LoanFilterDTO filterDTO, Pageable pageable);

	Page<Loan> getLoanByBook(Book book, Pageable pageable);

}
