package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.service.ReviewService;
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
@RequestMapping("/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid ReviewDto reviewDto) {
		log.info("Starting method save with ReviewDto: {}", reviewDto);
		reviewService.save(reviewDto);
		log.info("Ending method save: {}", reviewDto);

		return new ResponseEntity<>(reviewDto, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReviewDto> findById(@PathVariable("id") Long id) {
		log.info("Starting method findById with id: {}", id);
		final ReviewDto reviewDto = reviewService.findById(id);
		log.info("Ending method findById: {}", reviewDto);

		return new ResponseEntity<>(reviewDto, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Page<ReviewDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
												   @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<ReviewDto> reviewDtoPage = reviewService.findAll(pageable);
		log.info("Ending method findAll in ReviewController {}", reviewDtoPage);

		return new ResponseEntity<>(reviewDtoPage, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody @Valid ReviewDto reviewDtoUpdate) {
		log.info("Starting method update with ReviewDto: {}", reviewDtoUpdate);
		reviewService.update(reviewDtoUpdate);
		log.info("Ending method update: {}", reviewDtoUpdate);

		return new ResponseEntity<>(reviewDtoUpdate, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
		log.info("Starting method deleteById with id: {}", id);
		reviewService.deleteById(id);
		log.info("Ending method deleteById.");

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
