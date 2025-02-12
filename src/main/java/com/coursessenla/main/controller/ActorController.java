package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.service.ActorService;
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
@RequestMapping("/actors")
public class ActorController {

	private final ActorService actorService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void save(@RequestBody @Valid ActorDto actorDto) {
		log.info("Starting method save: {}", actorDto);
		actorService.save(actorDto);
		log.info("Ending method save: {}", actorDto);
	}

	@GetMapping("/id/{id}")
	public ActorDto findById(@PathVariable(value = "id") Long id) {
		log.info("Starting method findById with id: {}", id);
		final ActorDto actorDto = actorService.findById(id);
		log.info("Ending method findById {}", actorDto);

		return actorDto;
	}

	@GetMapping("/{name}")
	public ActorDto findByName(@PathVariable(value = "name") String name) {
		log.info("Starting method findByName with name: {}", name);
		final ActorDto actorDto = actorService.findByName(name);
		log.info("Ending method findByName: {}", actorDto);

		return actorDto;
	}

	@GetMapping()
	public Page<ActorDto> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
								  @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<ActorDto> actorDtoPage = actorService.findAll(pageable);
		log.info("Ending method findAlle: {}", actorDtoPage.getTotalElements());

		return actorDtoPage;
	}

	@PutMapping
	public void update(@RequestBody @Valid ActorDto actorDtoUpdate) {
		log.info("Starting method update with actorDtoUpdate: {}", actorDtoUpdate);
		actorService.update(actorDtoUpdate);
		log.info("Ending method update {}", actorDtoUpdate);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable(name = "id") Long id) {
		log.info("Starting method delete in with id: {}", id);
		actorService.deleteById(id);
		log.info("Ending method delete.");
	}
}
