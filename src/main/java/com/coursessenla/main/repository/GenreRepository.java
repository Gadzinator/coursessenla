package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Genre;

import java.util.Optional;

public interface GenreRepository {
	Optional<Genre> findByName(String name);
}
