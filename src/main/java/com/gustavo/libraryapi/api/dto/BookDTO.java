package com.gustavo.libraryapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Adiciona Getters, Setters, toString, equals e hashcode
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
	
	private Long id;
	private String title;
	private String author;
	private String isbn;

}
