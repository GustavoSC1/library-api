package com.gustavo.libraryapi.service.impl;

import org.springframework.stereotype.Service;

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
		return repository.save(book);
	}

}
