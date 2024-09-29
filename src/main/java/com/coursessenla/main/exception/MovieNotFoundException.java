package com.coursessenla.main.exception;

public class MovieNotFoundException extends RuntimeException {

	public MovieNotFoundException() {
		super("No movies were found");
	}

	public MovieNotFoundException(long id) {
		super(String.format("Movie with id %d was not found", id));
	}
}
