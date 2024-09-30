package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.domain.entity.CharacterInfo;
import com.coursessenla.main.domain.entity.CharacterInfoId;
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
class CharacterInfoRepositoryImplTest {

	@Resource
	private CharacterInfoRepositoryImpl characterInfoRepository;

	@Resource
	private ActorRepositoryImpl actorRepository;

	@Resource
	private MovieRepositoryImpl movieRepository;

	@Resource
	private GenreRepositoryImpl genreRepository;

	@Test
	void testSave() {
		final Genre genre = createGenre();
		genreRepository.save(genre);
		final Actor actor = createActor();
		final Movie movie = createMovie(genre);
		final CharacterInfo movieActor = createMovieActor(actor, movie);
		movieRepository.save(movie);
		actorRepository.save(actor);
		characterInfoRepository.save(movieActor);

		final Optional<CharacterInfo> optionalMovieActor = characterInfoRepository.findById(movieActor.getId());
		assertTrue(optionalMovieActor.isPresent());
		optionalMovieActor.ifPresent(m -> {
			assertEquals(movieActor.getActor(), m.getActor());
			assertEquals(movieActor.getMovie(), m.getMovie());
		});
	}

	@Test
	void testFindById() {
		final Genre genre = createGenre();
		genreRepository.save(genre);
		final Actor actor = createActor();
		final Movie movie = createMovie(genre);
		final CharacterInfo movieActor = createMovieActor(actor, movie);
		movieRepository.save(movie);
		actorRepository.save(actor);
		characterInfoRepository.save(movieActor);

		final Optional<CharacterInfo> optionalMovieActor = characterInfoRepository.findById(movieActor.getId());
		assertTrue(optionalMovieActor.isPresent());
		optionalMovieActor.ifPresent(m -> {
			assertEquals(movieActor.getActor(), m.getActor());
			assertEquals(movieActor.getMovie(), m.getMovie());
		});
	}

	@Test
	void testFindAll() {
		final Genre genre = createGenre();
		genreRepository.save(genre);
		final Actor firstActor = createActor();
		final Movie firstMovie = createMovie(genre);
		final Actor secondActor = createActor();
		final Movie secondMovie = createMovie(genre);
		final CharacterInfo firstMovieActor = createMovieActor(firstActor, firstMovie);
		final CharacterInfo secondMovieActor = createMovieActor(secondActor, secondMovie);
		movieRepository.save(firstMovie);
		actorRepository.save(firstActor);
		movieRepository.save(secondMovie);
		actorRepository.save(secondActor);
		characterInfoRepository.save(firstMovieActor);
		characterInfoRepository.save(secondMovieActor);

		Pageable pageable = PageRequest.of(0, 10);

		Page<CharacterInfo> characterInfoPage = characterInfoRepository.findAll(pageable);

		assertNotNull(characterInfoPage);
		assertEquals(10, characterInfoPage.getContent().size());
		assertEquals(52, characterInfoPage.getTotalElements());
	}

	@Test
	void testUpdate() {
		final Genre genre = createGenre();
		genreRepository.save(genre);
		final Actor actor = createActor();
		final Movie movie = createMovie(genre);
		final CharacterInfo movieActor = createMovieActor(actor, movie);
		movieRepository.save(movie);
		actorRepository.save(actor);
		characterInfoRepository.save(movieActor);
		movieActor.setCharacterName("Boss");
		characterInfoRepository.update(movieActor);

		final Optional<CharacterInfo> optionalMovieActor = characterInfoRepository.findById(movieActor.getId());
		assertTrue(optionalMovieActor.isPresent());
		optionalMovieActor.ifPresent(m ->
				assertEquals(movieActor.getCharacterName(), m.getCharacterName()));
	}

	@Test
	void testDeleteById() {
		final Genre genre = createGenre();
		genreRepository.save(genre);
		final Actor actor = createActor();
		final Movie movie = createMovie(genre);
		final CharacterInfo movieActor = createMovieActor(actor, movie);
		movieRepository.save(movie);
		actorRepository.save(actor);
		characterInfoRepository.save(movieActor);
		characterInfoRepository.deleteById(movieActor.getId());

		final Optional<CharacterInfo> optionalMovieActor = characterInfoRepository.findById(movieActor.getId());
		assertTrue(optionalMovieActor.isEmpty());
	}

	private Actor createActor() {
		Actor actor = new Actor();
		actor.setName("Leonardo DiCaprio");
		actor.setBirthDate(LocalDate.parse("1974-11-11"));
		actor.setNationality("American");
		actor.setAwards("Oscar");

		return actor;
	}

	private CharacterInfo createMovieActor(Actor actor, Movie movie) {
		CharacterInfo movieActor = new CharacterInfo();
		movieActor.setMovie(movie);
		movieActor.setActor(actor);
		movieActor.setCharacterName("Dom Cobb");

		CharacterInfoId movieActorId = new CharacterInfoId();
		movieActorId.setMovieId(movie.getId());
		movieActorId.setActorId(actor.getId());

		movieActor.setId(movieActorId);

		return movieActor;
	}

	private Movie createMovie(Genre genre) {
		Movie movie = new Movie();
		movie.setTitle("Inception");
		movie.setReleaseDate(LocalDate.parse("2017-10-16"));
		movie.setGenres(List.of(genre));
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
