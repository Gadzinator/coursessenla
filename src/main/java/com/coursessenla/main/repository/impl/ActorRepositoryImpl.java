package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.repository.ActorRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ActorRepositoryImpl implements ActorRepository {

	private final List<Actor> actors = new ArrayList<>();

	@Override
	public void save(Actor actor) {
		actors.add(actor);
	}

	@Override
	public Optional<Actor> findById(long id) {
		return actors.stream()
				.filter(actor -> actor.getId() == id)
				.findFirst();
	}

	@Override
	public Optional<Actor> findByName(String name) {
		return actors.stream()
				.filter(actor -> actor.getName().equals(name))
				.findFirst();
	}

	@Override
	public void updateById(long id, Actor actorUpdate) {
		for (int i = 0; i < actors.size(); i++) {
			Actor actor = actors.get(i);
			if (actor.getId() == id) {
				actors.set(i, actorUpdate);
			}
		}
	}

	@Override
	public void deleteById(long id) {
		actors.removeIf(actor -> actor.getId() == id);
	}
}
