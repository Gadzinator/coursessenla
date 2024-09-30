package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.domain.entity.Review;
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
class ReviewRepositoryImplTest {

	private static final long REVIEW_ID = 51;
	private static final String UPDATE_REVIEW_CONTENT = "Very Good";

	@Resource
	private ReviewRepositoryImpl reviewRepository;

	@Resource
	private UserRepositoryImpl userRepository;

	@Resource
	private MovieRepositoryImpl movieRepository;

	@Test
	void testSave() {
		final User user = createUser();
		final Movie movie = createMovie();
		final Review review = createReview(user, movie);
		userRepository.save(user);
		movieRepository.save(movie);
		reviewRepository.save(review);

		final Optional<Review> optionalReview = reviewRepository.findById(REVIEW_ID);
		assertTrue(optionalReview.isPresent());
		optionalReview.ifPresent(r -> {
			assertEquals(review.getContent(), r.getContent());
			assertEquals(review.getUser(), r.getUser());
			assertEquals(review.getMovie(), r.getMovie());
		});
	}

	@Test
	void testFindById() {
		final User user = createUser();
		final Movie movie = createMovie();
		final Review review = createReview(user, movie);
		userRepository.save(user);
		movieRepository.save(movie);
		reviewRepository.save(review);

		final Optional<Review> optionalReview = reviewRepository.findById(REVIEW_ID);
		assertTrue(optionalReview.isPresent());
		optionalReview.ifPresent(r -> {
			assertEquals(review.getContent(), r.getContent());
			assertEquals(review.getUser(), r.getUser());
			assertEquals(review.getMovie(), r.getMovie());
		});
	}

	@Test
	void testFindAll() {
		final User user = createUser();
		final Movie movie = createMovie();
		final Review review = createReview(user, movie);
		userRepository.save(user);
		movieRepository.save(movie);
		reviewRepository.save(review);

		Pageable pageable = PageRequest.of(0, 10);

		final Page<Review> reviewPage = reviewRepository.findAll(pageable);

		assertNotNull(reviewPage);
		assertEquals(10, reviewPage.getContent().size());
		assertEquals(51, reviewPage.getTotalElements());
	}

	@Test
	void testUpdate() {
		final User user = createUser();
		final Movie movie = createMovie();
		final Review review = createReview(user, movie);
		userRepository.save(user);
		movieRepository.save(movie);
		reviewRepository.save(review);
		review.setContent(UPDATE_REVIEW_CONTENT);
		reviewRepository.update(review);

		final Optional<Review> optionalReview = reviewRepository.findById(REVIEW_ID);
		assertTrue(optionalReview.isPresent());
		optionalReview.ifPresent(r ->
				assertEquals(UPDATE_REVIEW_CONTENT, r.getContent()));
	}

	@Test
	void testDeleteById() {
		final User user = createUser();
		final Movie movie = createMovie();
		final Review review = createReview(user, movie);
		userRepository.save(user);
		movieRepository.save(movie);
		reviewRepository.save(review);
		reviewRepository.deleteById(REVIEW_ID);

		final Optional<Review> optionalReview = reviewRepository.findById(REVIEW_ID);
		assertTrue(optionalReview.isEmpty());
	}

	private Review createReview(User user, Movie movie) {
		Review review = new Review();
		review.setContent("This is a great movie!");
		review.setRating(8.5);
		review.setUser(user);
		review.setMovie(movie);
		return review;
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
