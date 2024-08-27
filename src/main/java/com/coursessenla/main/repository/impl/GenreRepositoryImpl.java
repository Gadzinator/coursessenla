package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.repository.GenreRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

	private final List<Genre> genres = new ArrayList<>();

	@Override
	public void save(Genre genre) {
		genres.add(genre);
	}

	@Override
	public Optional<Genre> findById(long id) {
		return genres.stream()
				.filter(genre -> genre.getId() == id)
				.findFirst();
	}

	@Override
	public Optional<Genre> findByName(String name) {
		return genres.stream()
				.filter(genre -> genre.getName().equals(name))
				.findFirst();
	}

	@Override
	public List<Genre> findAll() {
		return genres;
	}

	@Override
	public void updateById(long id, Genre genreUpdate) {
		final OptionalInt indexOptional = IntStream.range(0, genres.size())
				.filter(i -> genres.get(i).getId() == id)
				.findFirst();

		indexOptional.ifPresent(index -> genres.set(index, genreUpdate));
	}

	@Override
	public void deleteById(long id) {
		genres.removeIf(genre -> genre.getId() == id);
	}
}
