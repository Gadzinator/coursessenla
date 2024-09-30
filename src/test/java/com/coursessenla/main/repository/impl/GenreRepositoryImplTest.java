package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Genre;
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

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class GenreRepositoryImplTest {

	private static final String UPDATE_GENRE_NAME = "Comedy Woman";
	private static final long GENRE_ID = 51;

	@Resource
	private GenreRepositoryImpl genreRepository;

	@Test
	void testSave() {
		final Genre genre = createGenre();
		genreRepository.save(genre);

		final Optional<Genre> optionalGenre = genreRepository.findById(GENRE_ID);
		assertTrue(optionalGenre.isPresent());
		optionalGenre.ifPresent(g -> {
			assertEquals(genre.getId(), g.getId());
			assertEquals(genre.getName(), g.getName());
		});
	}

	@Test
	void testFindById() {
		final Genre genre = createGenre();
		genreRepository.save(genre);

		final Optional<Genre> optionalGenre = genreRepository.findById(GENRE_ID);
		assertTrue(optionalGenre.isPresent());
		optionalGenre.ifPresent(g -> {
			assertEquals(genre.getId(), g.getId());
			assertEquals(genre.getName(), g.getName());
		});
	}

	@Test
	void testFindAll() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		genreRepository.save(firstGenre);
		genreRepository.save(secondGenre);

		Pageable pageable = PageRequest.of(0, 10);

		final Page<Genre> genrePage = genreRepository.findAll(pageable);
		assertNotNull(genrePage);
		assertEquals(10, genrePage.getContent().size());
		assertEquals(52, genrePage.getTotalElements());
	}

	@Test
	void testFindByName() {
		final Genre genre = createGenre();
		genreRepository.save(genre);

		final Optional<Genre> optionalGenre = genreRepository.findByName(genre.getName());
		assertNotNull(optionalGenre);
		optionalGenre.ifPresent(g ->
				assertEquals(genre.getName(), g.getName()));
	}

	@Test
	void testUpdate() {
		final Genre genre = createGenre();
		genreRepository.save(genre);
		genre.setName(UPDATE_GENRE_NAME);
		genreRepository.update(genre);

		final Optional<Genre> optionalGenre = genreRepository.findById(GENRE_ID);
		assertTrue(optionalGenre.isPresent());
		optionalGenre.ifPresent(g ->
				assertEquals(UPDATE_GENRE_NAME, g.getName()));
	}

	@Test
	void testDeleteById() {
		final Genre genre = createGenre();
		genreRepository.save(genre);
		genreRepository.deleteById(GENRE_ID);

		final Optional<Genre> deleteOptionalGenre = genreRepository.findById(GENRE_ID);
		assertTrue(deleteOptionalGenre.isEmpty());
	}

	private Genre createGenre() {
		Genre genre = new Genre();
		genre.setName("Actions");
		genre.setMovies(Collections.emptyList());

		return genre;
	}
}
