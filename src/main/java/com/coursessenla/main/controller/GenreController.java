package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {

	private final GenreService genreService;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid GenreDto genreDto) {
		log.info("Starting method save with GenreDto: {}", genreDto);
		genreService.save(genreDto);
		log.info("Ending method save: {}", genreDto);

		return new ResponseEntity<>("Genre saved", HttpStatus.CREATED);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<GenreDto> findById(@PathVariable("id") Long id) {
		log.info("Starting method findById with id: {}", id);
		final GenreDto genreDto = genreService.findById(id);
		log.info("Ending method findById: {}", genreDto);

		return new ResponseEntity<>(genreDto, HttpStatus.OK);
	}

	@GetMapping("/{name}")
	public ResponseEntity<GenreDto> findByName(@PathVariable("name") String name) {
		log.info("Starting method findByName with name: {}", name);
		final GenreDto genreDto = genreService.findByName(name);
		log.info("Ending method findByName: {}", genreDto);

		return new ResponseEntity<>(genreDto, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Page<GenreDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
												  @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<GenreDto> genreDtoPage = genreService.findAll(pageable);
		log.info("Ending method findAll: {}", genreDtoPage);

		return new ResponseEntity<>(genreDtoPage, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody @Valid GenreDto genreDtoUpdate) {
		log.info("Starting method update with GenreDto: {}", genreDtoUpdate);
		genreService.update(genreDtoUpdate);
		log.info("Ending method update: {}", genreDtoUpdate);

		return new ResponseEntity<>(genreDtoUpdate, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
		log.info("Starting method deleteById with id: {}", id);
		genreService.deleteById(id);
		log.info("Ending method deleteById.");

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
