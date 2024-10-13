package com.coursessenla.main.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeRequest {

	@NotBlank(message = "Old password should not be blank")
	private String oldPassword;

	@NotBlank(message = "newPassword should not be blank")
	@Size(min = 8, max = 25, message = "New password must be at least 8 characters long")
	private String newPassword;
}
