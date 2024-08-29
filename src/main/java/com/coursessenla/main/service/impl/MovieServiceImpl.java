package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.MovieRepository;
import com.coursessenla.main.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements com.coursessenla.main.service.MovieService {

	private final MovieRepository movieRepository;
	private final GenreService genreService;
	private final GenericMapper mapper;

	@Override
	public void save(MovieDto movieDto) {
		final List<String> genreNames = movieDto.getGenres().stream()
				.map(GenreDto::getName)
				.toList();

		final List<Genre> genreList = genreService.findAllByNames(genreNames)
				.stream()
				.map(genreDto -> mapper.mapToDto(genreDto, Genre.class))
				.toList();

		final Movie movie = mapper.mapToDto(movieDto, Movie.class);
		movie.setGenres(genreList);

		movieRepository.save(movie);
	}

	@Override
	public MovieDto findById(long id) {
		return movieRepository.findById(id)
				.map(movie -> mapper.mapToEntity(movie, MovieDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Movie with id %d was not found", id)));
	}

	@Override
	public List<MovieDto> findByGenre(String genreName) {
		List<Movie> movies = movieRepository.findByGenre(genreName);
		if (movies == null) {
			return Collections.emptyList();
		}

		return movies.stream()
				.map(movie -> mapper.mapToDto(movie, MovieDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void updateById(long id, MovieDto movieDtoUpdate) {
		findById(id);
		movieRepository.updateById(id, mapper.mapToDto(movieDtoUpdate, Movie.class));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		movieRepository.deleteById(id);
	}
}
