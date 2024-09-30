package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.exception.MovieNotFoundException;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.impl.MovieRepositoryImpl;
import com.coursessenla.main.service.impl.config.ServiceTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
class MovieServiceImplTest {

	private static final long MOVIE_ID = 1;
	private static final String ACTION_GENRE_NAME = "Action";
	private static final String DRAMA_GENRE_NAME = "Drama";

	@Mock
	private MovieRepositoryImpl movieRepository;

	@Mock
	private GenericMapperImpl mapper;

	@Mock
	private GenreServiceImpl genreService;

	@InjectMocks
	private MovieServiceImpl movieService;

	@Test
	void save() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		secondGenre.setId(2L);
		secondGenre.setName(DRAMA_GENRE_NAME);
		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto seconGenreDto = createGenreDto();
		seconGenreDto.setId(2L);
		seconGenreDto.setName(DRAMA_GENRE_NAME);

		List<Genre> existingGenres = List.of(firstGenre, secondGenre);
		final Movie movie = createMovie(existingGenres);

		List<GenreDto> existingGenresDto = List.of(firstGenreDto, seconGenreDto);
		final MovieDto movieDto = createMovieDto(existingGenresDto);
		when(genreService.findByName(ACTION_GENRE_NAME)).thenReturn(firstGenreDto);
		when(genreService.findByName(DRAMA_GENRE_NAME)).thenReturn(seconGenreDto);
		when(mapper.mapToEntity(firstGenreDto, Genre.class)).thenReturn(firstGenre);
		when(mapper.mapToEntity(seconGenreDto, Genre.class)).thenReturn(secondGenre);
		when(mapper.mapToEntity(movieDto, Movie.class)).thenReturn(movie);
		doNothing().when(movieRepository).save(movie);

		movieService.save(movieDto);

		verify(genreService).findByName(ACTION_GENRE_NAME);
		verify(genreService).findByName(DRAMA_GENRE_NAME);
		verify(mapper).mapToEntity(firstGenreDto, Genre.class);
		verify(mapper).mapToEntity(seconGenreDto, Genre.class);
		verify(mapper).mapToEntity(movieDto, Movie.class);
		verify(movieRepository).save(movie);
		assertEquals(2, movie.getGenres().size());
		assertEquals(ACTION_GENRE_NAME, movie.getGenres().get(0).getName());
		assertEquals(DRAMA_GENRE_NAME, movie.getGenres().get(1).getName());
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		secondGenre.setId(2L);
		secondGenre.setName(DRAMA_GENRE_NAME);

		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto secondGenreDto = createGenreDto();
		secondGenreDto.setId(2L);
		secondGenreDto.setName(DRAMA_GENRE_NAME);

		List<GenreDto> existingGenresDto = List.of(firstGenreDto, secondGenreDto);
		final MovieDto movieDto = createMovieDto(existingGenresDto);

		when(genreService.findByName(ACTION_GENRE_NAME)).thenReturn(firstGenreDto);
		when(genreService.findByName(DRAMA_GENRE_NAME)).thenReturn(secondGenreDto);
		when(mapper.mapToEntity(firstGenreDto, Genre.class)).thenReturn(firstGenre);
		when(mapper.mapToEntity(secondGenreDto, Genre.class)).thenReturn(secondGenre);
		when(mapper.mapToEntity(movieDto, Movie.class)).thenReturn(createMovie(List.of(firstGenre, secondGenre)));

		doThrow(new RuntimeException("Database error")).when(movieRepository).save(any(Movie.class));
		assertThrows(RuntimeException.class, () -> movieService.save(movieDto));

		verify(genreService).findByName(ACTION_GENRE_NAME);
		verify(genreService).findByName(DRAMA_GENRE_NAME);
		verify(mapper).mapToEntity(firstGenreDto, Genre.class);
		verify(mapper).mapToEntity(secondGenreDto, Genre.class);
		verify(mapper).mapToEntity(movieDto, Movie.class);
		verify(movieRepository).save(any(Movie.class));
	}

	@Test
	void testFindByIdWhenMovieExist() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		secondGenre.setId(2L);
		secondGenre.setName(DRAMA_GENRE_NAME);

		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto secondGenreDto = createGenreDto();
		secondGenreDto.setId(2L);
		secondGenreDto.setName(DRAMA_GENRE_NAME);

		List<GenreDto> existingGenresDto = List.of(firstGenreDto, secondGenreDto);
		final MovieDto movieDto = createMovieDto(existingGenresDto);
		List<Genre> existingGenres = List.of(firstGenre, secondGenre);
		final Movie movie = createMovie(existingGenres);

		when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
		when(mapper.mapToDto(firstGenre, GenreDto.class)).thenReturn(firstGenreDto);
		when(mapper.mapToDto(secondGenre, GenreDto.class)).thenReturn(secondGenreDto);
		when(mapper.mapToDto(movie, MovieDto.class)).thenReturn(movieDto);

		MovieDto result = movieService.findById(MOVIE_ID);

		verify(movieRepository).findById(MOVIE_ID);
		verify(mapper).mapToDto(firstGenre, GenreDto.class);
		verify(mapper).mapToDto(secondGenre, GenreDto.class);
		verify(mapper).mapToDto(movie, MovieDto.class);
		assertNotNull(result);
		assertEquals(MOVIE_ID, result.getId());
		assertEquals("Inception", result.getTitle());
		assertEquals(2, result.getGenres().size());
		assertEquals(DRAMA_GENRE_NAME, result.getGenres().get(1).getName());
	}

	@Test
	void testFindByIdWhenMovieNotExist() {
		when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.empty());

		assertThrows(MovieNotFoundException.class, () -> movieService.findById(MOVIE_ID));
	}

	@Test
	void findByGenreWhenMovieExist() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		secondGenre.setId(2L);
		secondGenre.setName(DRAMA_GENRE_NAME);

		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto secondGenreDto = createGenreDto();
		secondGenreDto.setId(2L);
		secondGenreDto.setName(DRAMA_GENRE_NAME);

		List<GenreDto> existingGenresDto = List.of(firstGenreDto, secondGenreDto);
		final MovieDto movieDto = createMovieDto(existingGenresDto);
		List<Genre> existingGenres = List.of(firstGenre, secondGenre);
		final Movie movie = createMovie(existingGenres);

		when(movieRepository.findByGenre(ACTION_GENRE_NAME)).thenReturn(List.of(movie));
		when(mapper.mapToDto(movie, MovieDto.class)).thenReturn(movieDto);

		final List<MovieDto> actualeMovieDtoList = movieService.findByGenre(ACTION_GENRE_NAME);

		verify(movieRepository).findByGenre(ACTION_GENRE_NAME);
		verify(mapper).mapToDto(movie, MovieDto.class);
		assertEquals(movieDto, actualeMovieDtoList.get(0));
	}

	@Test
	void findByGenreWhenMovieNotExist() {
		when(movieRepository.findByGenre(ACTION_GENRE_NAME)).thenReturn(Collections.emptyList());

		assertThrows(MovieNotFoundException.class, () -> movieService.findByGenre(ACTION_GENRE_NAME));
	}

	@Test
	void testFindAllWhenListMoviesNotEmpty() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		secondGenre.setId(2L);
		secondGenre.setName(DRAMA_GENRE_NAME);
		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto secondGenreDto = createGenreDto();
		secondGenreDto.setId(2L);
		secondGenreDto.setName(DRAMA_GENRE_NAME);

		List<GenreDto> existingGenresDto = List.of(firstGenreDto, secondGenreDto);

		final MovieDto firstMovieDto = createMovieDto(existingGenresDto);
		final MovieDto secondMovieDto = createMovieDto(existingGenresDto);
		secondMovieDto.setId(2L);

		List<Genre> existingGenres = List.of(firstGenre, secondGenre);
		final Movie firstMovie = createMovie(existingGenres);
		final Movie secondMovie = createMovie(existingGenres);
		secondMovie.setId(2L);

		List<Movie> movieList = Arrays.asList(firstMovie, secondMovie);

		Pageable pageable = PageRequest.of(0, 10);

		Page<Movie> moviePage = new PageImpl<>(movieList, pageable, movieList.size());

		when(movieRepository.findAll(pageable)).thenReturn(moviePage);
		when(mapper.mapToDto(firstMovie, MovieDto.class)).thenReturn(firstMovieDto);
		when(mapper.mapToDto(secondMovie, MovieDto.class)).thenReturn(secondMovieDto);

		Page<MovieDto> actualMovieDtoPage = movieService.findAll(pageable);

		verify(movieRepository).findAll(pageable);
		verify(mapper).mapToDto(firstMovie, MovieDto.class);
		verify(mapper).mapToDto(secondMovie, MovieDto.class);
		assertNotNull(actualMovieDtoPage);
		assertEquals(2, actualMovieDtoPage.getContent().size());
		assertTrue(actualMovieDtoPage.getContent().contains(firstMovieDto));
		assertTrue(actualMovieDtoPage.getContent().contains(secondMovieDto));
	}

	@Test
	void testFindAllWhenListMoviesEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(movieRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(MovieNotFoundException.class, () -> movieService.findAll(pageable));
	}

	@Test
	void testUpdateWhenMovieFound() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		secondGenre.setId(2L);
		secondGenre.setName(DRAMA_GENRE_NAME);

		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto secondGenreDto = createGenreDto();
		secondGenreDto.setId(2L);
		secondGenreDto.setName(DRAMA_GENRE_NAME);

		List<GenreDto> existingGenresDto = List.of(firstGenreDto, secondGenreDto);
		final MovieDto movieDto = createMovieDto(existingGenresDto);
		movieDto.setTitle("Title");
		List<Genre> existingGenres = List.of(firstGenre, secondGenre);
		final Movie movie = createMovie(existingGenres);


		when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));
		when(mapper.mapToDto(firstGenre, GenreDto.class)).thenReturn(firstGenreDto);
		when(mapper.mapToDto(secondGenre, GenreDto.class)).thenReturn(secondGenreDto);
		when(mapper.mapToDto(movie, MovieDto.class)).thenReturn(movieDto);
		when(mapper.mapToEntity(movieDto, Movie.class)).thenAnswer(invocation -> {
			final MovieDto dto = invocation.getArgument(0);
			movie.setTitle(dto.getTitle());
			return movie;
		});
		doNothing().when(movieRepository).update(movie);

		movieService.update(movieDto);

		verify(movieRepository).findById(MOVIE_ID);
		verify(mapper).mapToDto(firstGenre, GenreDto.class);
		verify(mapper).mapToDto(secondGenre, GenreDto.class);
		verify(mapper).mapToDto(movie, MovieDto.class);
		verify(mapper).mapToEntity(movieDto, Movie.class);
		verify(movieRepository).update(movie);
		assertEquals(movieDto.getTitle(), movie.getTitle());
		assertEquals(movieDto.getDescription(), movie.getDescription());
		assertEquals(movieDto.getGenres().size(), movie.getGenres().size());
		assertEquals(movieDto.getGenres().get(0).getName(), movie.getGenres().get(0).getName());
		assertEquals(movieDto.getGenres().get(1).getName(), movie.getGenres().get(1).getName());
	}

	@Test
	void testUpdateWhenMovieNotFound() {
		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto secondGenreDto = createGenreDto();
		secondGenreDto.setId(2L);
		secondGenreDto.setName(DRAMA_GENRE_NAME);

		List<GenreDto> existingGenresDto = List.of(firstGenreDto, secondGenreDto);
		final MovieDto movieDto = createMovieDto(existingGenresDto);

		when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.empty());

		assertThrows(MovieNotFoundException.class, () -> movieService.update(movieDto));
		verify(movieRepository).findById(MOVIE_ID);
		verify(movieRepository, never()).update(any(Movie.class));
	}

	@Test
	void testDeleteByIdWhenMovieExist() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		secondGenre.setId(2L);
		secondGenre.setName(DRAMA_GENRE_NAME);

		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto secondGenreDto = createGenreDto();
		secondGenreDto.setId(2L);
		secondGenreDto.setName(DRAMA_GENRE_NAME);

		List<GenreDto> existingGenresDto = List.of(firstGenreDto, secondGenreDto);
		final MovieDto movieDto = createMovieDto(existingGenresDto);
		movieDto.setTitle("Title");
		List<Genre> existingGenres = List.of(firstGenre, secondGenre);
		final Movie movie = createMovie(existingGenres);

		when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(movie));

		when(mapper.mapToDto(firstGenre, GenreDto.class)).thenReturn(firstGenreDto);
		when(mapper.mapToDto(secondGenre, GenreDto.class)).thenReturn(secondGenreDto);
		when(mapper.mapToDto(movie, MovieDto.class)).thenReturn(movieDto);

		doNothing().when(movieRepository).deleteById(MOVIE_ID);

		movieService.deleteById(MOVIE_ID);

		verify(movieRepository).findById(MOVIE_ID);
		verify(mapper).mapToDto(firstGenre, GenreDto.class);
		verify(mapper).mapToDto(secondGenre, GenreDto.class);
		verify(mapper).mapToDto(movie, MovieDto.class);
		verify(movieRepository).deleteById(MOVIE_ID);
	}

	@Test
	void testDeleteByIdWhenMovieNotExist() {
		when(movieRepository.findById(MOVIE_ID)).thenThrow(MovieNotFoundException.class);

		assertThrows(MovieNotFoundException.class, () -> movieService.deleteById(MOVIE_ID));
		verify(movieRepository, never()).deleteById(MOVIE_ID);
	}

	private Movie createMovie(List<Genre> genres) {
		Movie movie = new Movie();
		movie.setId(1L);
		movie.setTitle("Inception");
		movie.setReleaseDate(LocalDate.parse("2017-10-16"));
		movie.setGenres(genres);
		movie.setDescription("A mind-bending thriller where dreams are a reality.");
		movie.setRating(9.5);
		movie.setDirectors(new ArrayList<>());

		return movie;
	}

	private MovieDto createMovieDto(List<GenreDto> genreDtoList) {
		MovieDto movieDto = new MovieDto();
		movieDto.setId(1L);
		movieDto.setTitle("Inception");
		movieDto.setReleaseDate("2017-10-16");
		movieDto.setGenres(genreDtoList);
		movieDto.setDescription("A mind-bending thriller where dreams are a reality.");
		movieDto.setRating(9.5);
		movieDto.setDirectorDtoList(new ArrayList<>());

		return movieDto;
	}

	private Genre createGenre() {
		Genre genre = new Genre();
		genre.setId(1L);
		genre.setName(ACTION_GENRE_NAME);
		genre.setMovies(Collections.emptyList());

		return genre;
	}

	private GenreDto createGenreDto() {
		GenreDto genreDto = new GenreDto();
		genreDto.setId(1L);
		genreDto.setName(ACTION_GENRE_NAME);


		return genreDto;
	}
}
