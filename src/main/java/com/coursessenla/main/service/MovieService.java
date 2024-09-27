package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.MovieDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
	void save(MovieDto movieDto);

	MovieDto findById(long id);

	List<MovieDto> findByGenre(String genreName);

	Page<MovieDto> findAll(Pageable pageable);

	void update(MovieDto movieDtoUpdate);

	void deleteById(long id);
}
