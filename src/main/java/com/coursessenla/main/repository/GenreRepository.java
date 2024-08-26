package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
	void save(Genre genre);

	Optional<Genre> findById(long id);

	Optional<Genre> findByName(String name);

	List<Genre> findAll();

	void updateById(long id, Genre genreUpdate);

	void deleteById(long id);
}
