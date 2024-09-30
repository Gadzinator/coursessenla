package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.domain.entity.User;
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
class PlayListRepositoryImplTest {

	private static final long PLAYLIST_ID = 51;
	private static final String PLAYLIST_NAME = "Second playlist";

	@Resource
	private PlayListRepositoryImpl playListRepository;

	@Resource
	private MovieRepositoryImpl movieRepository;

	@Resource
	private UserRepositoryImpl userRepository;

	@Test
	void testSave() {
		final Movie movie = createMovie();
		final User user = createUser();
		final Playlist playlist = createPlaylist(user, movie);
		movieRepository.save(movie);
		userRepository.save(user);
		playListRepository.save(playlist);

		final Optional<Playlist> optionalPlaylist = playListRepository.findById(PLAYLIST_ID);
		assertTrue(optionalPlaylist.isPresent());
		optionalPlaylist.ifPresent(p -> {
			assertEquals(playlist.getId(), p.getId());
			assertEquals(playlist.getName(), p.getName());
			assertEquals(playlist.getMovies(), p.getMovies());
		});
	}

	@Test
	void testFindById() {
		final Movie movie = createMovie();
		final User user = createUser();
		final Playlist playlist = createPlaylist(user, movie);
		movieRepository.save(movie);
		userRepository.save(user);
		playListRepository.save(playlist);

		final Optional<Playlist> optionalPlaylist = playListRepository.findById(PLAYLIST_ID);
		assertTrue(optionalPlaylist.isPresent());
		optionalPlaylist.ifPresent(p -> {
			assertEquals(playlist.getId(), p.getId());
			assertEquals(playlist.getName(), p.getName());
			assertEquals(playlist.getMovies(), p.getMovies());
		});
	}

	@Test
	void testFindAll() {
		final Movie movie = createMovie();
		final User user = createUser();
		final Playlist firstPlaylist = createPlaylist(user, movie);
		final Playlist secondPlaylist = createPlaylist(user, movie);
		secondPlaylist.setName(PLAYLIST_NAME);
		movieRepository.save(movie);
		userRepository.save(user);
		playListRepository.save(firstPlaylist);
		playListRepository.save(secondPlaylist);

		Pageable pageable = PageRequest.of(0, 10);

		final Page<Playlist> playlistPage = playListRepository.findAll(pageable);

		assertNotNull(playlistPage);
		assertEquals(10, playlistPage.getContent().size());
		assertEquals(52, playlistPage.getTotalElements());
	}

	@Test
	void testUpdate() {
		final Movie movie = createMovie();
		final User user = createUser();
		final Playlist playlist = createPlaylist(user, movie);
		movieRepository.save(movie);
		userRepository.save(user);
		playListRepository.save(playlist);
		playlist.setName(PLAYLIST_NAME);
		playListRepository.update(playlist);

		final Optional<Playlist> optionalPlaylist = playListRepository.findById(PLAYLIST_ID);
		assertTrue(optionalPlaylist.isPresent());
		optionalPlaylist.ifPresent(p ->
				assertEquals(playlist.getName(), p.getName()));
	}

	@Test
	void testDeleteById() {
		final Movie movie = createMovie();
		final User user = createUser();
		final Playlist playlist = createPlaylist(user, movie);
		movieRepository.save(movie);
		userRepository.save(user);
		playListRepository.save(playlist);
		playListRepository.deleteById(PLAYLIST_ID);

		final Optional<Playlist> optionalPlaylist = playListRepository.findById(PLAYLIST_ID);
		assertTrue(optionalPlaylist.isEmpty());
	}

	private Playlist createPlaylist(User user, Movie movie) {
		Playlist playlist = new Playlist();
		playlist.setName("Test Playlist");
		playlist.setUser(user);
		playlist.setMovies(new ArrayList<>(List.of(movie)));
		return playlist;
	}

	private User createUser() {
		User user = new User();
		user.setEmail("testuser@example.com");
		user.setPassword("password");

		return user;
	}

	private Movie createMovie() {
		Movie movie = new Movie();
		movie.setTitle("Test Movie");
		movie.setReleaseDate(LocalDate.now());
		movie.setDescription("This is a test movie.");
		movie.setRating(7.5);
		movie.setGenres(new ArrayList<>(List.of(createGenre())));
		movie.setDirectors(new ArrayList<>());

		return movie;
	}

	private Genre createGenre() {
		Genre genre = new Genre();
		genre.setName("Science Fiction");

		return genre;
	}
}
