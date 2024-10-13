package com.coursessenla.main.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleDto {

	private Long id;

	@NotNull(message = "Name cannot be null")
	@Size(min = 1, max = 10, message = "Name must be between 1 and 10 characters long")
	private String name;
}
