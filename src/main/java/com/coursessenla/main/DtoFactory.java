package com.coursessenla.main;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.dto.UserDto;

import java.util.Arrays;
import java.util.List;

public class DtoFactory {

	public static ReviewDto createReviewDtoFirst(UserDto userDto, MovieDto movieDto) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setId(75L);
		reviewDto.setContent("A brilliant.");
		reviewDto.setRating(4.8);
		reviewDto.setUser(userDto);
		reviewDto.setMovie(movieDto);

		return reviewDto;
	}

	public static ReviewDto createReviewDtoSecond(UserDto userDto, MovieDto movieDto) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setId(76L);
		reviewDto.setContent("An emotional roller coaster.");
		reviewDto.setRating(4.5);
		reviewDto.setUser(userDto);
		reviewDto.setMovie(movieDto);

		return reviewDto;
	}

	public static UserDto createUserDtoFirst() {
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setEmail("client1@example.com");

		return userDto;
	}

	public static UserDto createUserDtoSecond() {
		UserDto userDto = new UserDto();
		userDto.setId(2L);
		userDto.setEmail("client2@example.com");

		return userDto;
	}

	public static MovieDto createMovieDtoFirst() {
		MovieDto movieDto = new MovieDto();
		movieDto.setId(1L);
		movieDto.setTitle("title1");
		movieDto.setDescription("Description of Movie1");
		movieDto.setReleaseDate("01-01-2024");
		movieDto.setGenres(Arrays.asList(createGenreDtoFirst(), createGenreDtoSecond()));
		movieDto.setRating(7.8);

		return movieDto;
	}

	public static MovieDto createMovieDtoSecond() {
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
