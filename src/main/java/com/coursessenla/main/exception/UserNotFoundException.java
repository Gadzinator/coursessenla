package com.coursessenla.main.exception;

public class UserNotFoundException extends RuntimeException {

	public UserNotFoundException() {
		super("No users were found");
	}

	public UserNotFoundException(long id) {
		super(String.format("User with id %d was not found", id));
	}

	public UserNotFoundException(String email) {
		super(String.format("User with email %s was not found", email));
	}
}
