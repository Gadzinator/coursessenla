package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.CharacterInfoId;
import com.coursessenla.main.service.CharacterInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/characterInfos")
public class CharacterInfoController {

	private final CharacterInfoService characterInfoService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public void save(@RequestBody @Valid CharacterInfoDto characterInfoDto) {
		log.info("Starting method save with CharacterInfoDto: {}", characterInfoDto);
		characterInfoService.save(characterInfoDto);
		log.info("Ending method save: {}", characterInfoDto);
	}

	@GetMapping("/id")
	public CharacterInfoDto findById(@RequestParam("movieId") Long movieId, @RequestParam("actorId") Long actorId) {
		log.info("Starting method findById with movieId: {} and actorId: {}", movieId, actorId);
		CharacterInfoId characterInfoId = new CharacterInfoId();
		characterInfoId.setMovieId(movieId);
		characterInfoId.setActorId(actorId);
		final CharacterInfoDto characterInfoDto = characterInfoService.findById(characterInfoId);

		log.info("Ending method findById: {}", characterInfoDto);

		return characterInfoDto;
	}

	@GetMapping
	public Page<CharacterInfoDto> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
										  @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<CharacterInfoDto> characterInfoDtoPage = characterInfoService.findAll(pageable);
		log.info("Ending method findAll: {}", characterInfoDtoPage);

		return characterInfoDtoPage;
	}

	@PutMapping
	public void update(@RequestBody @Valid CharacterInfoDto updateCharacterInfoDto) {
		log.info("Starting method update with CharacterInfoDto: {}", updateCharacterInfoDto);
		characterInfoService.update(updateCharacterInfoDto);
		log.info("Ending method update: {}", updateCharacterInfoDto);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/id")
	public void deleteById(@RequestParam("movieId") Long movieId, @RequestParam("actorId") Long actorId) {
		log.info("Starting method deleteById with movieId: {} and actorId: {}", movieId, actorId);
		CharacterInfoId characterInfoId = new CharacterInfoId();
		characterInfoId.setMovieId(movieId);
		characterInfoId.setActorId(actorId);
		characterInfoService.deleteById(characterInfoId);
		log.info("Ending method deleteById.");
	}
}
