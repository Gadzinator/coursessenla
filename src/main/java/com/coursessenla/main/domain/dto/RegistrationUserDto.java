package com.coursessenla.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegistrationUserDto {

	private long id;

	private String firstName;

	private String lastName;

	private String password;

	private String confirmPassword;

	private String email;
}
