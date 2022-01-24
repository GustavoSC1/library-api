package com.gustavo.libraryapi.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gustavo.libraryapi.exception.BusinessException;
import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.repository.BookRepository;
import com.gustavo.libraryapi.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	
	private BookRepository repository;
	
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
		return this.repository.findById(id);
	}

	@Override
	public void delete(Book book) {
		if(book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}
		this.repository.delete(book);
	}

	@Override
	public Book update(Book book) {
		if(book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}
		return this.repository.save(book);
	}

	@Override
	public Page<Book> find(Book filter, Pageable pageRequest) {
		
		// Query by Example
		// A consulta por exemplo (QBE) é uma técnica de consulta amigável com uma interface simples. Ele permite a criação de 
		// consultas dinâmicas e não exige que você escreva consultas que contenham nomes de campos e nem que você escreva 
		// consultas usando linguagens de consulta específicas.
		
		Example<Book> example = Example.of(filter, 
				ExampleMatcher
					.matching() // Permite que as configurações sejam feitas
					.withIgnoreCase()// Nos campos string, vai verificar no banco ignorando se o usuário passou valor em cauxa alta ou em caixa baixa
					.withIgnoreNullValues()// Se foi passado alguma propriedade nula, será ignorada
					.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));// Vai informar se comparação das string serão feitas pelo início, pelo fim, em qualquer parte ou o valor exato que foi passado
		
		return repository.findAll(example, pageRequest);
	}

	@Override
	public Optional<Book> getBookByIsbn(String isbn) {
		return repository.findByIsbn(isbn);
	}

}
