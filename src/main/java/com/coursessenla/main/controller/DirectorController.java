package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.service.DirectorService;
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

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/directors")
public class DirectorController {

	private final DirectorService directorService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void save(@RequestBody @Valid DirectorDto directorDto) {
		log.info("Starting method save with DirectorDto: {}", directorDto);
		directorService.save(directorDto);
		log.info("Ending method save: {}", directorDto);
	}

	@GetMapping("/id/{id}")
	public DirectorDto findById(@PathVariable("id") Long id) {
		log.info("Starting method findById with id: {}", id);
		final DirectorDto directorDto = directorService.findById(id);
		log.info("Ending method findById: {}", directorDto);

		return directorDto;
	}

	@GetMapping("/{name}")
	public DirectorDto findByName(@PathVariable("name") String name) {
		log.info("Starting method findByName with name: {}", name);
		final DirectorDto directorDto = directorService.findByName(name);
		log.info("Ending method findByName: {}", directorDto);

		return directorDto;
	}

	@GetMapping
	public Page<DirectorDto> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
									 @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<DirectorDto> directorDtoPage = directorService.findAll(pageable);
		log.info("Ending method findAll: {}", directorDtoPage);

		return directorDtoPage;
	}

	@PutMapping
	public void update(@RequestBody @Valid DirectorDto directorDtoUpdate) {
		log.info("Starting method update with DirectorDto: {}", directorDtoUpdate);
		directorService.update(directorDtoUpdate);
		log.info("Ending method update: {}", directorDtoUpdate);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable("id") Long id) {
		log.info("Starting method deleteById with id: {}", id);
		directorService.deleteById(id);
		log.info("Ending method deleteById.");
	}
}
