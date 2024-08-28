package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.repository.DirectorRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

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
		final OptionalInt indexOptional = IntStream.range(0, directors.size())
				.filter(i -> directors.get(i).getId() == id)
				.findFirst();

		indexOptional.ifPresent(index -> directors.set(index, directorUpdate));
	}

	@Override
	public void deleteById(long id) {
		directors.removeIf(director -> director.getId() == id);
	}
}
