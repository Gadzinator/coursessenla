package com.coursessenla.main.exception;

public class GenreNotFoundException extends RuntimeException {

	public GenreNotFoundException() {
		super("No genre were found");
	}

	public GenreNotFoundException(String name) {
		super(String.format("Genre with name %s was not found", name));
	}

	public GenreNotFoundException(long id) {
		super(String.format("Genre with id %d was not found", id));
	}
}
