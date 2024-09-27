package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.GenreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenreService {
	void save(GenreDto genreDto);

	GenreDto findById(long id);

	GenreDto findByName(String name);

	Page<GenreDto> findAll(Pageable pageable);

	void update(GenreDto genreDtoUpdate);

	void deleteById(long id);
}
