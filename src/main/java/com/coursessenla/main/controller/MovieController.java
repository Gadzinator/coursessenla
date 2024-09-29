package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController {

	private final MovieService movieService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void save(@RequestBody @Valid MovieDto movieDto) {
		log.info("Starting method save with MovieDto: {}", movieDto);
		movieService.save(movieDto);
		log.info("Ending method save: {}", movieDto);
	}

	@GetMapping("/id/{id}")
	public MovieDto findById(@PathVariable("id") Long id) {
		log.info("Starting method findById with id: {}", id);
		final MovieDto movieDto = movieService.findById(id);
		log.info("Ending method findById: {}", movieDto);

		return movieDto;
	}

	@GetMapping("/{genreName}")
	public List<MovieDto> findByName(@PathVariable("genreName") String genreName) {
		log.info("Starting method findByName with genreName: {}", genreName);
		final List<MovieDto> movieDtoList = movieService.findByGenre(genreName);
		log.info("Ending method findByName: {}", movieDtoList);

		return movieDtoList;
	}

	@GetMapping
	public Page<MovieDto> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
								  @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<MovieDto> movieDtoPage = movieService.findAll(pageable);
		log.info("Ending method findAll: {}", movieDtoPage);

		return movieDtoPage;
	}

	@PutMapping
	public void update(@RequestBody @Valid MovieDto movieDtoUpdate) {
		log.info("Starting method update with MovieDto: {}", movieDtoUpdate);
		movieService.update(movieDtoUpdate);
		log.info("Ending method update: {}", movieDtoUpdate);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable("id") Long id) {
		log.info("Starting method deleteById with id: {}", id);
		movieService.deleteById(id);
		log.info("Ending method deleteById.");
	}
}
