package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Actor;

import java.util.Optional;

public interface ActorRepository {
	Optional<Actor> findByName(String name);
}
