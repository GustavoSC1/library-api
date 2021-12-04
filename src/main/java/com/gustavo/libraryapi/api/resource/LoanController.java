package com.gustavo.libraryapi.api.resource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gustavo.libraryapi.api.dto.BookDTO;
import com.gustavo.libraryapi.api.dto.LoanDTO;
import com.gustavo.libraryapi.api.dto.LoanFilterDTO;
import com.gustavo.libraryapi.api.dto.ReturnedLoanDTO;
import com.gustavo.libraryapi.model.entity.Book;
import com.gustavo.libraryapi.model.entity.Loan;
import com.gustavo.libraryapi.service.BookService;
import com.gustavo.libraryapi.service.LoanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor // Gera um construtor com argumentos necessários (Ex: final e @NonNull)
@Api("Loan API")
public class LoanController {
	
	private final LoanService loanService;
	private final BookService bookService; 
	private final ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation("Create a loan")
	@ApiResponses(value = {
            @ApiResponse(code = 201, message = "Loan successfully created")
    })
	public Long create(@RequestBody LoanDTO dto) {
		Book book = bookService.getBookByIsbn(dto.getIsbn())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found for passed isbn"));
		Loan entity = Loan.builder().book(book)
									.customer(dto.getCustomer())
									.loanDate(LocalDate.now())
									.build();
		
		entity = loanService.save(entity);
		return entity.getId();
	}
	
	@PatchMapping("{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation("Returns a book")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book returned successfully")
    })
	public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {
		Loan loan = loanService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		loan.setReturned(dto.getReturned());
		loanService.update(loan);		
	}
	
	@GetMapping
	@ApiOperation("Find loans by params")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Loans found successfully ")
    })
	public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageRequest) {
		Page<Loan> result = loanService.find(dto, pageRequest);
		List<LoanDTO> loans = result
				.getContent()
				.stream()
				.map(entity -> {
					Book book = entity.getBook();
					BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
					LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
					loanDTO.setBook(bookDTO);
					return loanDTO;
				}).collect(Collectors.toList());
		return new PageImpl<LoanDTO>(loans, pageRequest, result.getTotalElements());
	}

}
