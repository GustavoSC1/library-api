package com.gustavo.libraryapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.gustavo.libraryapi.exception.BusinessException;
import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.repository.BookRepository;
import com.gustavo.libraryapi.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	private BookRepository repository;
	
	public BookServiceImpl() {
		
	}
	
	public BookServiceImpl(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	public Book save(Book book) {
		if(repository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn já cadastrado.");
		}
		return repository.save(book);
	}

	@Override
	public Optional<Book> getById(Long id) {
		return null;
	}

	@Override
	public void delete(Book book) {
				
	}

	@Override
	public Book update(Book book) {
		return null;
	}

}
