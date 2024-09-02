package com.coursessenla.main.exception;

public class FailedTransaction extends RuntimeException {

	public FailedTransaction(String message, Throwable cause) {
		super(message, cause);
	}
}
