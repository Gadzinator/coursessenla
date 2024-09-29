package com.coursessenla.main.exception;

public class ActorNotFoundException extends RuntimeException {

	public ActorNotFoundException() {
		super("No actors were found");
	}

	public ActorNotFoundException(String name) {
		super(String.format("Actor with name %s was not found", name));
	}

	public ActorNotFoundException(long id) {
		super(String.format("Actor with id %d was not found", id));
	}
}
