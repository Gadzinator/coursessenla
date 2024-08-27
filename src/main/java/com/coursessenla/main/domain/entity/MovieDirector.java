package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
public class MovieDirector {

	private List<Long> movieId;
	private Long directorId;
}
