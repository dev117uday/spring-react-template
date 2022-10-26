package com.example.jwt.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ExceptionBroker.class)
	public ResponseEntity<String> ResponseEntityExceptionHandler(ExceptionBroker exception) {
		return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
	}
}
