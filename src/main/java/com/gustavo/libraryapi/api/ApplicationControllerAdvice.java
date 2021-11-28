package com.gustavo.libraryapi.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.gustavo.libraryapi.api.exception.ApiErrors;
import com.gustavo.libraryapi.exception.BusinessException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleValidationException(MethodArgumentNotValidException ex) {
		// Vai conter todas as mensagens de erro que ocorreram ao tentar validar o objeto
		BindingResult bindingResult =  ex.getBindingResult();
		return new ApiErrors(bindingResult);
	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiErrors handleBusinessException(BusinessException ex) {
		return new ApiErrors(ex);
	}
	
	@ExceptionHandler(ResponseStatusException.class)
	@ResponseStatus
	public ResponseEntity handleResponseStatusException(ResponseStatusException ex) {
		return new ResponseEntity(new ApiErrors(ex), ex.getStatus());
	}

}
