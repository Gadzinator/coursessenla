package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.repository.DirectorRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DirectorRepositoryImpl implements DirectorRepository {

	private final List<Director> directors = new ArrayList<>();

	@Override
	public void save(Director director) {
		directors.add(director);
	}

	@Override
	public Optional<Director> findById(long id) {
		return directors.stream()
				.filter(director -> director.getId() == id)
				.findFirst();
	}

	@Override
	public Optional<Director> findByName(String name) {
		return directors.stream()
				.filter(director -> director.getName().equals(name))
				.findFirst();
	}

	@Override
	public void updateById(long id, Director directorUpdate) {
		for (int i = 0; i < directors.size(); i++) {
			Director director = directors.get(i);
			if (director.getId() == id) {
				directors.set(i, directorUpdate);
			}
		}
	}

	@Override
	public void deleteById(long id) {
		directors.removeIf(director -> director.getId() == id);
	}
}
