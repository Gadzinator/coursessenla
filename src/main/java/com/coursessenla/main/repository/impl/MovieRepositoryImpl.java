package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.repository.MovieRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class MovieRepositoryImpl implements MovieRepository {

	private final List<Movie> movies = new ArrayList<>();

	@Override
	public void save(Movie movie) {
		movies.add(movie);
	}

	@Override
	public Optional<Movie> findById(long id) {
		return movies.stream().filter(movie -> movie.getId() == id).findFirst();
	}

	@Override
	public List<Movie> findByGenre(String genreName) {
		return movies.stream()
				.filter(movie -> movie.getGenres().stream()
						.allMatch(genre -> genre.getName().equals(genreName)))
				.collect(Collectors.toList());
	}

	@Override
	public void updateById(long id, Movie movieUpdate) {
		final OptionalInt indexOptional = IntStream.range(0, movies.size())
				.filter(i -> movies.get(i).getId() == id)
				.findFirst();

		indexOptional.ifPresent(index -> movies.set(index, movieUpdate));
	}

	@Override
	public void deleteById(long id) {
		movies.removeIf(movie -> movie.getId() == id);
	}
}
