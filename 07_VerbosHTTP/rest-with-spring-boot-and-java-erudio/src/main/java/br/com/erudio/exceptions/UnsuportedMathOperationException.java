package br.com.erudio.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsuportedMathOperationException extends RuntimeException{

	public UnsuportedMathOperationException(String ex) {
		super(ex);
	}

	private static final long serialVersionUID = 1L;

}
