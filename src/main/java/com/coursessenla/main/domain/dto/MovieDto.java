package com.coursessenla.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MovieDto {

	private Long id;

	@NotNull(message = "Title cannot be null")
	@Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters long")
	private String title;

	@NotNull(message = "Description cannot be null")
	@Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters long")
	private String description;

	@JsonFormat(pattern = "dd-MM-yyyy")
	@NotNull(message = "ReleaseDate cannot be null")
	private String releaseDate;

	@NotNull(message = "Genres cannot be null")
	@Size(min = 1, message = "There must me ay least one genre")
	private List<GenreDto> genres;

	@NotNull(message = "Director cannot be null")
	private Double rating;

	@NotNull(message = "Director cannot be null")
	@Size(min = 1, message = "There must me ay least one director")
	private List<DirectorDto> directorDtoList;
}
