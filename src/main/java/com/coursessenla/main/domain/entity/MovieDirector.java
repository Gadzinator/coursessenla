package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class MovieDirector {

	private List<Long> movieId;
	private Long directorId;
}
