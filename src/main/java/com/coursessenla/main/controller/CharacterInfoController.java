package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.CharacterInfoId;
import com.coursessenla.main.service.CharacterInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CharacterInfoController {

	private final CharacterInfoService characterInfoService;
	private final JsonUtils jsonUtils;

	public void save(CharacterInfoDto characterInfoDto) {
		log.info("Starting method save with CharacterInfoDto: {}", characterInfoDto);

		characterInfoService.save(characterInfoDto);

		final String json = jsonUtils.getJson(characterInfoDto);
		log.info("Ending method save: {}", json);
	}

	public CharacterInfoDto findById(CharacterInfoId id) {
		log.info("Starting method findById with id: {}", id);

		final CharacterInfoDto characterInfoDto = characterInfoService.findById(id);

		final String json = jsonUtils.getJson(characterInfoDto);
		log.info("Ending method findById: {}", json);

		return characterInfoDto;
	}

	public List<CharacterInfoDto> findAll() {
		log.info("Starting method findAll");

		final List<CharacterInfoDto> infoDtoList = characterInfoService.findAll();

		final String json = jsonUtils.getJson(infoDtoList);
		log.info("Ending method findAll: {}", json);

		return infoDtoList;
	}

	public void update(CharacterInfoDto updateCharacterInfoDto) {
		log.info("Starting method update with CharacterInfoDto: {}", updateCharacterInfoDto);

		characterInfoService.update(updateCharacterInfoDto);

		final String json = jsonUtils.getJson(updateCharacterInfoDto);
		log.info("Ending method update: {}", json);
	}

	public void deleteById(CharacterInfoId id) {
		log.info("Starting method deleteById with id: {}", id);

		characterInfoService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", String.format("CharacterInfo with Id %s has been successfully deleted", id));
		String json = jsonUtils.getJson(response);
		System.out.println(json);
		log.info("Ending method deleteById. Deletion response: {}", json);
	}
}
