package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.GenreDto;

import java.util.List;

public interface GenreService {
	void save(GenreDto genreDto);

	GenreDto findById(long id);

	GenreDto findByName(String name);

	List<GenreDto> findAll();

	void update(GenreDto genreDtoUpdate);

	List<GenreDto> findAllByNames(List<String> genreNames);

	void deleteById(long id);
}
