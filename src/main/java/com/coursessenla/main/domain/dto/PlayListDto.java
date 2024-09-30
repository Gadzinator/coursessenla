package com.coursessenla.main.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlayListDto {

	private Long id;

	@NotNull(message = "Name cannot be null")
	@Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters long")
	private String name;

	@NotNull(message = "User id cannot be null")
	private Long userId;
}
