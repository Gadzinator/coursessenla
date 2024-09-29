package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.exception.MovieNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.MovieRepositoryImpl;
import com.coursessenla.main.service.GenreService;
import com.coursessenla.main.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {

	private final MovieRepositoryImpl movieRepository;
	private final GenreService genreService;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public void save(MovieDto movieDto) {
		log.info("Starting method save: {}", movieDto);
		final List<Genre> genreList = movieDto.getGenres().stream()
				.map(GenreDto::getName)
				.map(genreService::findByName)
				.map(genreDto -> mapper.mapToEntity(genreDto, Genre.class))
				.toList();

		final Movie movie = mapper.mapToEntity(movieDto, Movie.class);
		movie.setGenres(genreList);

		movieRepository.save(movie);
		log.info("Ending method save: {}", movieDto);
	}

	@Transactional
	@Override
	public MovieDto findById(long id) {
		log.info("Starting method findById: {}", id);
		final Movie movie = movieRepository.findById(id)
				.orElseThrow(() -> new MovieNotFoundException(id));

		List<GenreDto> genreDtoList = movie.getGenres().stream()
				.map(genre -> mapper.mapToDto(genre, GenreDto.class))
				.collect(Collectors.toList());

		MovieDto movieDto = mapper.mapToDto(movie, MovieDto.class);
		movieDto.setGenres(genreDtoList);

		log.info("Ending method findById: {}", movieDto);

		return movieDto;
	}

	@Override
	public List<MovieDto> findByGenre(String genreName) {
		log.info("Starting method findByGenre: {}", genreName);
		List<Movie> movies = movieRepository.findByGenre(genreName);
		if (movies.isEmpty()) {
			log.info("No movies found for genre: {}", genreName);
			throw new MovieNotFoundException();
		}

		final List<MovieDto> movieDtoList = movies.stream()
				.map(movie -> mapper.mapToDto(movie, MovieDto.class))
				.collect(Collectors.toList());
		log.info("Ending method findByGenre: {}", movieDtoList);

		return movieDtoList;
	}

	@Transactional
	@Override
	public Page<MovieDto> findAll(Pageable pageable) {
		log.info("Starting method findAll: {}", pageable);
		final Page<MovieDto> movieDtoPage = movieRepository.findAll(pageable)
				.map(movie -> mapper.mapToDto(movie, MovieDto.class));

		if (movieDtoPage.isEmpty()) {
			log.info("No movies found for genre");
			throw new MovieNotFoundException();
		}
		log.info("Ending method findAll: {}", movieDtoPage);

		return movieDtoPage;
	}

	@Transactional
	@Override
	public void update(MovieDto movieDtoUpdate) {
		log.info("Starting method update: {}", movieDtoUpdate);
		findById(movieDtoUpdate.getId());
		movieRepository.update(mapper.mapToEntity(movieDtoUpdate, Movie.class));
		log.info("Ending method update: {}", movieDtoUpdate);
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting method deleteById: {}", id);
		findById(id);
		movieRepository.deleteById(id);
		log.info("Ending method deleteById: {}", id);
	}
}
