package com.coursessenla.main.exception;

public class DirectorNotFoundException extends RuntimeException {

	public DirectorNotFoundException() {
		super("No director were found");
	}

	public DirectorNotFoundException(String name) {
		super(String.format("Director with name %s was not found", name));
	}

	public DirectorNotFoundException(long id) {
		super(String.format("Director with id %d was not found", id));
	}
}
