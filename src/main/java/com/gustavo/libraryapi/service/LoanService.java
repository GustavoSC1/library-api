package com.gustavo.libraryapi.service;

import java.util.Optional;

import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.entity.Loan;

public interface LoanService {
	
	Loan save(Loan loan);

	Optional<Loan> getById(Long id);

	Loan update(Loan loan);

}
