package com.capone.lob.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class LimitOrderNotFoundException extends Exception{
	
	public LimitOrderNotFoundException(String message){
		super(message);
	}
}
