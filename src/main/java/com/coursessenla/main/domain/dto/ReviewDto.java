package com.coursessenla.main.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewDto {

	private Long id;

	@NotNull(message = "Content cannot be null")
	@Size(min = 10, max = 30, message = "Review content must be between 10 and 30 characters")
	private String content;

	@NotNull(message = "Rating cannot be null")
	@Min(value = 0, message = "Rating must be at least 0")
	@Max(value = 10, message = "Rating must not exceed 10")
	private Double rating;

	@NotNull(message = "User cannot be null")
	private UserDto user;

	@NotNull(message = "Movie cannot be null")
	private MovieDto movie;
}
