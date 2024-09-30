package com.coursessenla.main.exception;

import com.coursessenla.main.domain.entity.CharacterInfoId;

public class CharacterInfoNotFoundException extends RuntimeException {

	public CharacterInfoNotFoundException() {
		super("No characterInfo were found");
	}

	public CharacterInfoNotFoundException(CharacterInfoId id) {
		super(String.format("CharacterInfo with id %s was not found", id));
	}
}
