package com.gustavo.libraryapi.service;

import java.util.Optional;

import com.gustavo.libraryapi.model.entity.Book;

public interface BookService {

	Book save(Book any);
	
	Optional<Book> getById(Long id);

}
