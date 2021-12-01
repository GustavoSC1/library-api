package com.gustavo.libraryapi.service.impl;

import java.util.Optional;

import com.gustavo.libraryapi.exception.BusinessException;
import com.gustavo.libraryapi.model.entity.Loan;
import com.gustavo.libraryapi.model.repository.LoanRepository;
import com.gustavo.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {
	
	private LoanRepository repository;
	
	public LoanServiceImpl(LoanRepository repository) {
		this.repository = repository;
	}

	@Override
	public Loan save(Loan loan) {
		if(repository.existsByBookAndNotReturned(loan.getBook())) {
			throw new BusinessException("Book already loaned");
		}
		return repository.save(loan);
	}

	@Override
	public Optional<Loan> getById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Loan update(Loan loan) {
		return null;
	}

}
