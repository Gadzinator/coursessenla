package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Movie;

import java.util.List;

public interface MovieRepository {
	List<Movie> findByGenre(String genreName);
}
