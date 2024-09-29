package com.coursessenla.main.exception;

public class ReviewNotFoundException extends RuntimeException {

	public ReviewNotFoundException() {
		super("No reviews were found");
	}

	public ReviewNotFoundException(long id) {
		super(String.format("Review with id %d was not found", id));
	}
}
