package com.gustavo.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gustavo.libraryapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
