package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Data
@Component
public class Movie {

	private long id;
	private String title;
	private String description;
	private LocalDate releaseDate;
	private List<Genre> genres;
	private double rating;
}
