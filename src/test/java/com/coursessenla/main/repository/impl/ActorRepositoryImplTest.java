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
class ActorRepositoryImplTest {

	private static final long ACTOR_ID = 51;

	@Resource
	private ActorRepositoryImpl actorRepository;

	@Resource
	private MovieRepositoryImpl movieRepository;

	@Resource
	private CharacterInfoRepositoryImpl characterInfoRepository;

	@Test
	public void testSave() {
		final Movie movie = createMovie();
		movieRepository.save(movie);
		final Actor actor = createActor();
		actorRepository.save(actor);

		final Optional<Actor> optionalActor = actorRepository.findById(ACTOR_ID);
		assertTrue(optionalActor.isPresent());
		final Actor retrievedActor = optionalActor.get();
		assertEquals(actor.getId(), retrievedActor.getId());
		assertEquals(actor.getName(), retrievedActor.getName());
		assertEquals(actor.getAwards(), retrievedActor.getAwards());
	}

	@Test
	void testFindById() {
		final Movie movie = createMovie();
		movieRepository.save(movie);
		final Actor actor = createActor();
		actorRepository.save(actor);

		final Optional<Actor> optionalActor = actorRepository.findById(ACTOR_ID);
		assertTrue(optionalActor.isPresent());
		final Actor retrievedActor = optionalActor.get();
		assertEquals(actor.getId(), retrievedActor.getId());
		assertEquals(actor.getName(), retrievedActor.getName());
	}

	@Test
	void testFindByName() {
		final Movie movie = createMovie();
		movieRepository.save(movie);
		final Actor actor = createActor();
		actorRepository.save(actor);
		final CharacterInfo characterInfo = createMovieActor(actor, movie);
		characterInfoRepository.save(characterInfo);

		final Optional<Actor> optionalActor = actorRepository.findByName(actor.getName());
		assertTrue(optionalActor.isPresent());
		final Actor retrievedActor = optionalActor.get();
		assertEquals(actor.getId(), retrievedActor.getId());
		assertEquals(actor.getName(), retrievedActor.getName());
	}

	@Test
	void testFindAllWithPagination() {
		final Movie movie = createMovie();
		movieRepository.save(movie);

		final Actor firstActor = createActor();
		final Actor secondActor = createActor();
		actorRepository.save(firstActor);
		actorRepository.save(secondActor);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Actor> actorPage = actorRepository.findAll(pageable);

		assertNotNull(actorPage);
		assertEquals(10, actorPage.getContent().size());
		assertEquals(52, actorPage.getTotalElements());
	}

	@Test
	void testUpdate() {
		final Movie movie = createMovie();
		movieRepository.save(movie);
		final Actor actor = createActor();
		actorRepository.save(actor);
		actor.setName("ALex DiCaprio");
		actorRepository.update(actor);

		final Actor retrievedActor = actorRepository.findById(actor.getId()).orElse(null);
		assertNotNull(retrievedActor);
		assertEquals(actor.getName(), retrievedActor.getName());
	}

	@Test
	void testDeleteById() {
		final Movie movie = createMovie();
		movieRepository.save(movie);
		final Actor actor = createActor();
		actorRepository.save(actor);
		final long actorId = actor.getId();
		actorRepository.deleteById(actorId);

		final Optional<Actor> deleteActorOptional = actorRepository.findById(actorId);
		assertTrue(deleteActorOptional.isEmpty());
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
