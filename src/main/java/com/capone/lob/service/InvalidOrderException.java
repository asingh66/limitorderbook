package com.capone.lob.service;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST)
public class InvalidOrderException extends Exception {
	public InvalidOrderException(String message) {
		super(message);
	}
}
