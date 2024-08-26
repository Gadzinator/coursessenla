package com.coursessenla.main;

import com.coursessenla.main.controller.ActorController;
import com.coursessenla.main.controller.DirectorController;
import com.coursessenla.main.controller.GenreController;
import com.coursessenla.main.controller.MovieController;
import com.coursessenla.main.controller.PlayListController;
import com.coursessenla.main.controller.ReviewController;
import com.coursessenla.main.controller.UserController;
import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.List;

@ComponentScan
public class Application {

	public static void main(String[] args) throws JsonProcessingException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
		final UserController userController = context.getBean(UserController.class);
		final ActorController actorController = context.getBean(ActorController.class);
		final DirectorController directorController = context.getBean(DirectorController.class);
		final GenreController genreController = context.getBean(GenreController.class);
		final MovieController movieController = context.getBean(MovieController.class);
		final PlayListController playListController = context.getBean(PlayListController.class);
		final ReviewController reviewController = context.getBean(ReviewController.class);

		RegistrationUserDto registrationUserFirst = new RegistrationUserDto();
		registrationUserFirst.setId(1);
		registrationUserFirst.setFirstName("John");
		registrationUserFirst.setLastName("Doe");
		registrationUserFirst.setPassword("password123");
		registrationUserFirst.setConfirmPassword("password123");
		registrationUserFirst.setEmail("john.doe@example.com");

		RegistrationUserDto registrationUserSecond = new RegistrationUserDto();
		registrationUserSecond.setId(2);
		registrationUserSecond.setFirstName("Jane");
		registrationUserSecond.setLastName("Smith");
		registrationUserSecond.setPassword("password456");
		registrationUserSecond.setConfirmPassword("password456");
		registrationUserSecond.setEmail("jane.smith@example.com");

		final UserDto userDtoFirst = userController.createNewUser(registrationUserFirst);
		final UserDto userDtoSecond = userController.createNewUser(registrationUserSecond);
		userController.findById(1);
		userController.deleteById(2);
		userController.findById(1);

		ActorDto actorDtoFirst = new ActorDto();
		actorDtoFirst.setId(1);
		actorDtoFirst.setName("Leonardo DiCaprio");
		actorDtoFirst.setBirthDate("11-11-1974");
		actorDtoFirst.setNationality("American");
		actorDtoFirst.setAwards("Oscar, Golden Globe");

		ActorDto actorDtoSecond = new ActorDto();
		actorDtoSecond.setId(2);
		actorDtoSecond.setName("Emma Watson");
		actorDtoSecond.setBirthDate("15-04-1990");
		actorDtoSecond.setNationality("British");
		actorDtoSecond.setAwards("MTV Movie Award, Saturn Award");

		actorController.save(actorDtoFirst);
		actorController.save(actorDtoSecond);
		actorController.findById(1);
		actorController.delete(2);
		actorController.updateById(1, actorDtoSecond);
		actorController.findById(2);

		DirectorDto directorDtoFirst = new DirectorDto();
		directorDtoFirst.setId(1);
		directorDtoFirst.setName("Christopher Nolan");
		directorDtoFirst.setBirthDate("07-30-1970");
		directorDtoFirst.setNationality("British-American");
		directorDtoFirst.setAwards("Oscar, Golden Globe");

		DirectorDto directorDtoSecond = new DirectorDto();
		directorDtoSecond.setId(2);
		directorDtoSecond.setName("Greta Gerwig");
		directorDtoSecond.setBirthDate("08-04-1983");
		directorDtoSecond.setNationality("American");
		directorDtoSecond.setAwards("Golden Globe, BAFTA");

		directorController.save(directorDtoFirst);
		directorController.save(directorDtoSecond);
		directorController.findById(1);
		directorController.deleteById(2);
		directorController.updateById(1, directorDtoSecond);
		directorController.findById(2);

		GenreDto genreDtoFirst = new GenreDto();
		genreDtoFirst.setId(1L);
		genreDtoFirst.setName("Action");

		GenreDto genreDtoSecond = new GenreDto();
		genreDtoSecond.setId(2L);
		genreDtoSecond.setName("Comedy");

		genreController.save(genreDtoFirst);
		genreController.save(genreDtoSecond);
		genreController.findById(1);
		genreController.deleteById(2);
		genreController.updateById(1, genreDtoSecond);
		genreController.findById(2);

		MovieDto movieDtoFirst = new MovieDto();
		movieDtoFirst.setId(1L);
		movieDtoFirst.setTitle("Action Movie");
		movieDtoFirst.setDescription("An exciting action-packed adventure.");
		movieDtoFirst.setReleaseDate("05-12-2020");
		movieDtoFirst.setGenres(List.of(genreDtoFirst));
		movieDtoFirst.setRating(8.5);

		MovieDto movieDtoSecond = new MovieDto();
		movieDtoSecond.setId(2L);
		movieDtoSecond.setTitle("Comedy Drama");
		movieDtoSecond.setDescription("A heartwarming comedy with dramatic elements.");
		movieDtoSecond.setReleaseDate("03-10-2023");
		movieDtoSecond.setGenres(Arrays.asList(genreDtoFirst, genreDtoSecond));
		movieDtoSecond.setRating(7.8);

		movieController.save(movieDtoFirst);
		movieController.save(movieDtoSecond);
		movieController.findById(1);
		movieController.deleteById(2);
		movieController.updateById(1, movieDtoSecond);
		movieController.findById(2);

		PlayListDto playlistDtoFirst = new PlayListDto();
		playlistDtoFirst.setId(1L);
		playlistDtoFirst.setName("Chill Vibes");
		playlistDtoFirst.setUserId(1L);

		PlayListDto playlistDtoSecond = new PlayListDto();
		playlistDtoSecond.setId(2L);
		playlistDtoSecond.setName("Workout Mix");
		playlistDtoSecond.setUserId(2L);

		playListController.save(playlistDtoFirst);
		playListController.save(playlistDtoSecond);
		playListController.findById(1);
		playListController.deleteById(2);
		playListController.updateById(1, playlistDtoSecond);
		playListController.findById(2);

		ReviewDto reviewDtoFirst = new ReviewDto();
		reviewDtoFirst.setId(1L);
		reviewDtoFirst.setContent("A brilliant performance and direction. Must watch!");
		reviewDtoFirst.setRating(4.8);
		reviewDtoFirst.setUser(userDtoFirst);
		reviewDtoFirst.setMovie(movieDtoFirst);

		ReviewDto reviewDtoSecond = new ReviewDto();
		reviewDtoSecond.setId(2L);
		reviewDtoSecond.setContent("An emotional roller coaster with stunning visuals.");
		reviewDtoSecond.setRating(4.5);
		reviewDtoSecond.setUser(userDtoSecond);
		reviewDtoSecond.setMovie(movieDtoSecond);

		reviewController.save(reviewDtoFirst);
		reviewController.save(reviewDtoSecond);
		reviewController.findById(1);
		reviewController.deleteById(2);
		reviewController.updateById(1, reviewDtoSecond);
		reviewController.findById(2);
	}
}
