package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.service.PlayListService;
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
@RequestMapping("/playlists")
public class PlayListController {

	private final PlayListService playListService;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody @Valid PlayListDto playListDto) {
		log.info("Starting method save with PlayListDto: {}", playListDto);
		playListService.save(playListDto);
		log.info("Ending method save: {}", playListDto);

		return new ResponseEntity<>(playListDto, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlayListDto> findById(@PathVariable("id") Long id) {
		log.info("Starting method findById with id: {}", id);
		final PlayListDto playListDto = playListService.findById(id);
		log.info("Ending method findById: {}", playListDto);

		return new ResponseEntity<>(playListDto, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Page<PlayListDto>> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
													 @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<PlayListDto> playListDtoPage = playListService.findAll(pageable);
		log.info("Ending method findAll: {}", playListDtoPage);

		return new ResponseEntity<>(playListDtoPage, HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<?> update(@RequestBody @Valid PlayListDto playListDtoUpdate) {
		log.info("Starting method update with PlayListDto: {}", playListDtoUpdate);
		playListService.update(playListDtoUpdate);
		log.info("Ending method update: {}", playListDtoUpdate);

		return new ResponseEntity<>(playListDtoUpdate, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
		log.info("Starting method deleteById with id: {}", id);
		playListService.deleteById(id);
		log.info("Ending method deleteById.");

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
