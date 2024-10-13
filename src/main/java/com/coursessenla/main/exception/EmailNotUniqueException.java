package com.coursessenla.main.exception;

public class EmailNotUniqueException extends RuntimeException {

	public EmailNotUniqueException(String email) {
		super(String.format("Email '%s' is already in use.", email));
	}
}
