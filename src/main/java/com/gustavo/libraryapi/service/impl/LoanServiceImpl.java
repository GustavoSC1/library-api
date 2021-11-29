package com.gustavo.libraryapi.service.impl;

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
		return repository.save(loan);
	}

}
