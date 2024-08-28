package com.coursessenla.main.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDto {

	private Long id;
	private String title;
	private String description;
	private String releaseDate;
	private List<GenreDto> genres;
	private double rating;
}
