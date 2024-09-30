package com.coursessenla.main.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationUserDto {

	private Long id;

	@NotNull(message = "First name cannot be null")
	@Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters long")
	private String firstName;

	@NotNull(message = "Last name cannot be null")
	@Size(min = 1, max = 50, message = "last name must be between 1 and 50 characters long")
	private String lastName;

	@NotNull(message = "Password cannot be null")
	@Size(min = 8, max = 25, message = "Password must be at least 8 characters long")
	private String password;

	@NotNull(message = "ConfirmPassword cannot be null")
	@Size(min = 8, max = 25, message = "ConfirmPassword must be at least 8 characters long")
	private String confirmPassword;

	@NotNull(message = "Email cannot be null")
	@Email(message = "Email should be valid")
	private String email;
}
