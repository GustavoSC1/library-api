package com.gustavo.libraryapi.api.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gustavo.libraryapi.api.dto.BookDTO;
import com.gustavo.libraryapi.api.dto.LoanDTO;
import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.entity.Loan;
import com.gustavo.libraryapi.service.BookService;
import com.gustavo.libraryapi.service.LoanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/books")
@Api("Book API")
@Slf4j
public class BookController {
	
	private BookService service;
	private ModelMapper modelMapper;
	private LoanService loanService;
	
	public BookController(BookService service, ModelMapper mapper, LoanService loanService) {
		this.service = service;
		this.modelMapper = mapper;
		this.loanService = loanService;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Create a book")
	@ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book successfully created")
    })
	public BookDTO create(@RequestBody @Valid BookDTO dto) {
		log.info("creating a book for isbn: {}", dto.getIsbn());
		Book entity = modelMapper.map(dto, Book.class);
		entity = service.save(entity);
		return modelMapper.map(entity, BookDTO.class);
	}
	
	@GetMapping("{id}")
	@ApiOperation("Obtains a book details by id")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book obtained successfully")
    })
	public BookDTO get(@PathVariable Long id) {
		log.info("obtaining details for book id: {}", id);
		return service.getById(id).map(book -> modelMapper.map(book, BookDTO.class))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); 
		//ResponseStatusException é uma alternativa programática para @ResponseStatus e é a classe 
		//base para exceções usadas para aplicar um código de status a uma resposta HTTP. 
		//https://stackabuse.com/how-to-return-http-status-codes-in-a-spring-boot-application/
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation("Deletes a book by id")
	@ApiResponses(value = {
            @ApiResponse(code = 204, message = "Book succesfully deleted")
    })
	public void delete(@PathVariable Long id ) {
		log.info("deleting book of id: {}", id);
		Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		service.delete(book);
	}
	
	@PutMapping("{id}")
	@ApiOperation("Updates a book")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully edited book")
    })
	public BookDTO update(@PathVariable Long id, BookDTO dto) {
		log.info("updating book of id: {}", id);
		return service.getById(id).map(book -> {
			
			book.setAuthor(dto.getAuthor());
			book.setTitle(dto.getTitle());
			book = service.update(book);
			return modelMapper.map(book, BookDTO.class);
			
		}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		
	}
	
	@GetMapping
	@ApiOperation("Find books by params")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Books found successfully ")
    })
	public Page<BookDTO> find(BookDTO dto, Pageable pageRequest) {
		Book filter = modelMapper.map(dto, Book.class);
		
		Page<Book> result = service.find(filter, pageRequest);
		
		List<BookDTO> list = result.getContent().stream()
				.map(entity -> modelMapper.map(entity, BookDTO.class))
				.collect(Collectors.toList());
		
		return new PageImpl<BookDTO>(list, pageRequest, result.getTotalElements());
	}
	
	@GetMapping("{id}/loans")
	@ApiOperation("Find loans by book")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loans found successfully")
    })
	public Page<LoanDTO> loanByBook(@PathVariable Long id, Pageable pageable) {
		Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Page<Loan> result =  loanService.getLoanByBook(book, pageable);
		List<LoanDTO> list = result.getContent()
				.stream()
				.map(loan -> {
					Book loanBook = loan.getBook();
					BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
					LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
					loanDTO.setBook(bookDTO);
					return loanDTO;
				}).collect(Collectors.toList());
		return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
	}
	
}
