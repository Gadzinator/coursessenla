package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Director;

import java.util.Optional;

public interface DirectorRepository {
	void save(Director director);

	Optional<Director> findById(long id);

	Optional<Director> findByName(String name);

	void updateById(long id, Director directorUpdate);

	void deleteById(long id);
}
