package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Review {

	private long id;
	private String content;
	private double rating;
	private User user;
	private Movie movie;
}
