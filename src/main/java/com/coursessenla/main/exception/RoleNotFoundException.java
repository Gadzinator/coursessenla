package com.coursessenla.main.exception;

public class RoleNotFoundException extends RuntimeException {

	public RoleNotFoundException() {
		super("No actors were found");
	}

	public RoleNotFoundException(long id) {
		super(String.format("Role with id %d was not found", id));
	}

	public RoleNotFoundException(String name) {
		super(String.format("Role with name %s was not found", name));
	}
}
