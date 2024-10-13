package com.coursessenla.main.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRoleChangeRequest {

	private String userName;

	@NotBlank(message = "newRole should not be blank")
	@Size(min = 4, max = 10)
	private String newRole;
}
