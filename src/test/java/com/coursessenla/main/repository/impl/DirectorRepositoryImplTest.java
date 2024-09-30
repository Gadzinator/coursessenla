package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.repository.impl.config.H2Config;
import com.coursessenla.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
class DirectorRepositoryImplTest {

	private static final long DIRECTOR_ID = 51;

	@Resource
	private DirectorRepositoryImpl directorRepository;

	@Test
	void testSave() {
		final Director director = createDirector();
		directorRepository.save(director);

		final Optional<Director> optionalDirector = directorRepository.findById(DIRECTOR_ID);
		assertTrue(optionalDirector.isPresent());
		final Director retrievedDirector = optionalDirector.get();
		assertEquals(director.getId(), retrievedDirector.getId());
		assertEquals(director.getName(), retrievedDirector.getName());
		assertEquals(director.getBirthDate(), retrievedDirector.getBirthDate());
		assertEquals(director.getMovies(), retrievedDirector.getMovies());
	}

	@Test
	void testFindById() {
		final Director director = createDirector();
		directorRepository.save(director);

		final Optional<Director> optionalDirector = directorRepository.findById(DIRECTOR_ID);
		assertTrue(optionalDirector.isPresent());
		final Director retrievedDirector = optionalDirector.get();
		assertEquals(director.getId(), retrievedDirector.getId());
		assertEquals(director.getMovies().size(), retrievedDirector.getMovies().size());
	}

	@Test
	void testFindAll() {
		final Director firstDirector = createDirector();
		final Director secondDirector = createDirector();
		directorRepository.save(firstDirector);
		directorRepository.save(secondDirector);

		Pageable pageable = PageRequest.of(0, 10);

		Page<Director> directorPage = directorRepository.findAll(pageable);

		assertNotNull(directorPage);
		assertEquals(10, directorPage.getContent().size());
		assertEquals(52, directorPage.getTotalElements());
	}

	@Test
	void testFindByName() {
		final Director director = createDirector();
		directorRepository.save(director);

		final Optional<Director> optionalDirector = directorRepository.findByName(director.getName());
		assertTrue(optionalDirector.isPresent());
		final Director retrievedDirector = optionalDirector.get();
		assertEquals(director.getId(), retrievedDirector.getId());
		assertEquals(director.getName(), retrievedDirector.getName());
		assertEquals(director.getMovies().size(), retrievedDirector.getMovies().size());
	}

	@Test
	void testUpdate() {
		final Director director = createDirector();
		directorRepository.save(director);
		director.setName("Pedro Pascal");
		directorRepository.update(director);

		final Director retrievedDirector = directorRepository.findById(director.getId()).orElse(null);
		assertNotNull(retrievedDirector);
		assertEquals(director.getName(), retrievedDirector.getName());
	}

	@Test
	void testDeleteById() {
		final Director director = createDirector();
		directorRepository.save(director);
		directorRepository.deleteById(DIRECTOR_ID);

		final Optional<Director> deleteOptionalDirector = directorRepository.findById(DIRECTOR_ID);
		assertTrue(deleteOptionalDirector.isEmpty());
	}

	private Director createDirector() {
		List<Movie> movies = new ArrayList<>();
		movies.add(createMovie());
		Director director = new Director();
		director.setName("Christopher Nolan");
		director.setBirthDate(LocalDate.parse("1970-10-17"));
		director.setNationality("British-American");
		director.setAwards("Oscar, Golden Globe");
		director.setMovies(movies);

		return director;
	}

	private Movie createMovie() {
		Movie movie = new Movie();
		movie.setTitle("Inception");
		movie.setReleaseDate(LocalDate.parse("2017-10-16"));
		movie.setGenres(List.of(createGenre()));
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
