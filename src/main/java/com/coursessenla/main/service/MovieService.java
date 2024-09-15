package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.MovieDto;

import java.util.List;

public interface MovieService {
	void save(MovieDto movieDto);

	MovieDto findById(long id);

	List<MovieDto> findByGenre(String genreName);

	List<MovieDto> findAll();

	void update(MovieDto movieDtoUpdate);

	void deleteById(long id);
}
