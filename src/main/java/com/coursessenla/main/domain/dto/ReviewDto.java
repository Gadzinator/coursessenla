package com.coursessenla.main.domain.dto;

import lombok.Data;

@Data
public class ReviewDto {

	private long id;
	private String content;
	private double rating;
	private UserDto user;
	private MovieDto movie;
}
