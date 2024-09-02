package com.coursessenla.main;

import com.coursessenla.main.controller.ReviewController;
import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.execute.MyRunnable;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.List;

@ComponentScan
public class Application {

	public static void main(String[] args) throws JsonProcessingException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
		final ReviewController reviewController = context.getBean(ReviewController.class);
		final MovieDto movieDtoFirst = createMovieDtoFirst();
		final MovieDto movieDtoSecond = createMovieDtoSecond();
		Thread myThread = new Thread(new MyRunnable(), "MyThread");
		myThread.start();
		Thread myThread1 = new Thread(new MyRunnable(), "MyThread1");
		myThread1.start();
		reviewController.save(createReviewDtoFirst(createUserDtoFirst(), movieDtoFirst));
		reviewController.save(createReviewDtoSecond(createUserDtoSecond(), movieDtoSecond));
	}

	private static ReviewDto createReviewDtoFirst(UserDto userDto, MovieDto movieDto) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setId(61L);
		reviewDto.setContent("A brilliant.");
		reviewDto.setRating(4.8);
		reviewDto.setUser(userDto);
		reviewDto.setMovie(movieDto);

		return reviewDto;
	}

	private static ReviewDto createReviewDtoSecond(UserDto userDto, MovieDto movieDto) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setId(62L);
		reviewDto.setContent("An emotional roller coaster.");
		reviewDto.setRating(4.5);
		reviewDto.setUser(userDto);
		reviewDto.setMovie(movieDto);

		return reviewDto;
	}

	private static UserDto createUserDtoFirst() {
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setEmail("client1@example.com");

		return userDto;
	}

	private static UserDto createUserDtoSecond() {
		UserDto userDto = new UserDto();
		userDto.setId(2L);
		userDto.setEmail("client2@example.com");

		return userDto;
	}

	private static MovieDto createMovieDtoFirst() {
		MovieDto movieDto = new MovieDto();
		movieDto.setId(1L);
		movieDto.setTitle("title1");
		movieDto.setDescription("Description of Movie1");
		movieDto.setReleaseDate("01-01-2024");
		movieDto.setGenres(Arrays.asList(createGenreDtoFirst(), createGenreDtoSecond()));
		movieDto.setRating(7.8);

		return movieDto;
	}

	private static MovieDto createMovieDtoSecond() {
		MovieDto movieDto = new MovieDto();
		movieDto.setId(2L);
		movieDto.setTitle("Movie2");
		movieDto.setDescription("Description of Movie2");
		movieDto.setReleaseDate("02-01-2024");
		movieDto.setGenres(List.of(createGenreDtoFirst()));
		movieDto.setRating(8.5);
		return movieDto;
	}

	private static GenreDto createGenreDtoFirst() {
		GenreDto genreDto = new GenreDto();
		genreDto.setId(1L);
		genreDto.setName("Action");

		return genreDto;
	}

	private static GenreDto createGenreDtoSecond() {
		GenreDto genreDto = new GenreDto();
		genreDto.setId(3L);
		genreDto.setName("Comedy");

		return genreDto;
	}
}
