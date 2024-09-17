package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.service.DirectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DirectorController {

	private final DirectorService directorService;
	private final JsonUtils jsonUtils;

	public void save(DirectorDto directorDto) {
		log.info("Starting method save with DirectorDto: {}", directorDto);

		directorService.save(directorDto);

		final String json = jsonUtils.getJson(directorDto);
		log.info("Ending method save: {}", json);
	}

	public DirectorDto findById(long id) {
		log.info("Starting method findById with id: {}", id);

		final DirectorDto directorDto = directorService.findById(id);

		final String json = jsonUtils.getJson(directorDto);
		log.info("Ending method findById: {}", json);

		return directorDto;
	}

	public DirectorDto findByName(String name) {
		log.info("Starting method findByName with name: {}", name);

		final DirectorDto directorDto = directorService.findByName(name);

		final String json = jsonUtils.getJson(directorDto);
		log.info("Ending method findByName: {}", json);

		return directorDto;
	}

	public List<DirectorDto> findAll() {
		log.info("Starting method findAll");

		final List<DirectorDto> directorDtoList = directorService.findAll();

		final String json = jsonUtils.getJson(directorDtoList);
		log.info("Ending method findAll: {}", json);

		return directorDtoList;
	}

	public void update(DirectorDto directorDtoUpdate) {
		log.info("Starting method update with DirectorDto: {}", directorDtoUpdate);

		directorService.update(directorDtoUpdate);

		final String json = jsonUtils.getJson(directorDtoUpdate);
		log.info("Ending method update: {}", json);
	}

	public void deleteById(long id) {
		log.info("Starting method deleteById with id: {}", id);

		directorService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", String.format("Director with Id %d has been successfully deleted", id));
		String json = jsonUtils.getJson(response);
		log.info("Ending method deleteById. Deletion response : {}", json);
	}
}
