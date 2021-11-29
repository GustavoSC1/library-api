package com.gustavo.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.libraryapi.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

}
