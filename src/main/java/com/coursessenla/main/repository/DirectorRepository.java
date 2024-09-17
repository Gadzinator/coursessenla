package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Director;

import java.util.Optional;

public interface DirectorRepository {
	Optional<Director> findByName(String name);
}
