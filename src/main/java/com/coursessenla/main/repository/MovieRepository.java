package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
	void save(Movie movie);

	Optional<Movie> findById(long id);

	List<Movie> findByGenre(String genreName);

	void updateById(long id, Movie movieUpdate);

	void deleteById(long id);
}
