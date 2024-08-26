package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class MovieGenre {

	private List<Long> movieId;
	private List<Long> genreId;
}
