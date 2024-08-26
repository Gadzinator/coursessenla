package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.mapper.GenreMapper;
import com.coursessenla.main.mapper.MovieMapper;
import com.coursessenla.main.repository.MovieRepository;
import com.coursessenla.main.service.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MovieServiceImpl implements com.coursessenla.main.service.MovieService {

	private final MovieRepository movieRepository;
	private final GenreService genreService;
	private final MovieMapper mapper;
	private final GenreMapper genreMapper;

	@Override
	public void save(MovieDto movieDto) {
		final List<String> genreNames = movieDto.getGenres().stream()
				.map(GenreDto::getName)
				.toList();

		final List<Genre> genreList = genreService.findAllByNames(genreNames)
				.stream()
				.map(genreMapper::toEntity)
				.toList();

		final Movie movie = mapper.toEntity(movieDto);
		movie.setGenres(genreList);

		movieRepository.save(movie);
	}

	@Override
	public MovieDto findById(long id) {
		return movieRepository.findById(id)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Movie with id %d was not found", id)));
	}

	@Override
	public List<MovieDto> findByGenre(String genreName) {
		List<Movie> movies = movieRepository.findByGenre(genreName);
		if (movies == null) {
			return Collections.emptyList();
		}

		return movies.stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public void updateById(long id, MovieDto movieDtoUpdate) {
		findById(id);
		movieRepository.updateById(id, mapper.toEntity(movieDtoUpdate));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		movieRepository.deleteById(id);
	}
}
