package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Actor;

import java.util.Optional;

public interface ActorRepository {
	void save(Actor actor);

	Optional<Actor> findById(long id);

	Optional<Actor> findByName(String name);

	void updateById(long id, Actor actorUpdate);

	void deleteById(long id);
}
