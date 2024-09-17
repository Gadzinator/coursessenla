package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.repository.impl.config.H2Config;
import com.coursessenla.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class MovieRepositoryImplTest {

	private static final long MOVIE_ID = 51;

	@Resource
	private MovieRepositoryImpl movieRepository;

	@Resource
	private GenreRepositoryImpl genreRepository;

	@Test
	void testSave() {
		final Genre genre = createGenre();
		final Movie movie = createMovie(genre);
		genreRepository.save(genre);
		movieRepository.save(movie);

		final Optional<Movie> optionalMovie = movieRepository.findById(MOVIE_ID);
		assertTrue(optionalMovie.isPresent());
		optionalMovie.ifPresent(m -> {
			assertEquals(movie.getId(), m.getId());
			assertEquals(movie.getGenres(), m.getGenres());
		});
	}

	@Test
	void testFindById() {
		final Genre genre = createGenre();
		final Movie movie = createMovie(genre);
		genreRepository.save(genre);
		movieRepository.save(movie);

		final Optional<Movie> optionalMovie = movieRepository.findById(MOVIE_ID);
		assertTrue(optionalMovie.isPresent());
		optionalMovie.ifPresent(m -> {
			assertEquals(movie.getId(), m.getId());
			assertEquals(movie.getGenres(), m.getGenres());
		});
	}

	@Test
	void testFindAll() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		final Movie firstMovie = createMovie(firstGenre);
		final Movie secondMovie = createMovie(firstGenre);
		genreRepository.save(firstGenre);
		genreRepository.save(secondGenre);
		movieRepository.save(firstMovie);
		movieRepository.save(secondMovie);

		final List<Movie> movieList = movieRepository.findAll();
		assertNotNull(movieRepository);
		assertTrue(movieList.contains(firstMovie));
		assertTrue(movieList.contains(secondMovie));
	}

	@Test
	void testFindByGenre() {
		final Genre genre = createGenre();
		final Movie movie = createMovie(genre);
		genreRepository.save(genre);
		movieRepository.save(movie);

		final List<Movie> movieList = movieRepository.findByGenre(genre.getName());
		assertNotNull(movieRepository);
		assertTrue(movieList.contains(movie));
	}

	@Test
	void testUpdate() {
		final Genre genre = createGenre();
		final Movie movie = createMovie(genre);
		genreRepository.save(genre);
		movieRepository.save(movie);
		movie.setRating(10.0);
		movieRepository.update(movie);

		final Optional<Movie> optionalMovie = movieRepository.findById(MOVIE_ID);
		assertTrue(optionalMovie.isPresent());
		optionalMovie.ifPresent(m -> {
			assertEquals(movie.getId(), m.getId());
			assertEquals(movie.getRating(), m.getRating());
		});
	}

	@Test
	void testDeleteById() {
		final Genre genre = createGenre();
		final Movie movie = createMovie(genre);
		genreRepository.save(genre);
		movieRepository.save(movie);
		movieRepository.deleteById(MOVIE_ID);

		final Optional<Movie> optionalMovie = movieRepository.findById(MOVIE_ID);
		assertTrue(optionalMovie.isEmpty());
	}

	private Movie createMovie(Genre genre) {
		List<Genre> genreList = new ArrayList<>();
		genreList.add(genre);
		Movie movie = new Movie();
		movie.setTitle("Inception");
		movie.setReleaseDate(LocalDate.parse("2017-10-16"));
		movie.setGenres(genreList);
		movie.setDescription("A mind-bending thriller where dreams are a reality.");
		movie.setRating(9.5);
		movie.setDirectors(new ArrayList<>());

		return movie;
	}

	private Genre createGenre() {
		Genre genre = new Genre();
		genre.setName("Science Fiction");

		return genre;
	}
}
