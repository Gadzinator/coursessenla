package com.coursessenla.main.mapper;

import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.entity.Movie;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MovieMapper {

	private final ModelMapper mapper;

	public MovieDto toDto(Movie movie) {
		return mapper.map(movie, MovieDto.class);
	}

	public Movie toEntity(MovieDto movieDto) {
		return mapper.map(movieDto, Movie.class);
	}
}
