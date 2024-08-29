package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.service.DirectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class DirectorController {

	private final DirectorService directorService;
	private final JsonUtils jsonUtils;

	public void save(DirectorDto directorDto) {
		directorService.save(directorDto);

		final String json = jsonUtils.getJson(directorDto);
		log.info("Method to save to DirectorController - " + json);
	}

	public DirectorDto findById(long id) {
		final DirectorDto directorDto = directorService.findById(id);

		final String json = jsonUtils.getJson(directorDto);
		log.info("Method to findById to DirectorController - " + json);

		return directorDto;
	}

	public DirectorDto findByName(String name) {
		final DirectorDto directorDto = directorService.findByName(name);

		final String json = jsonUtils.getJson(directorDto);
		log.info("Method to findByName to DirectorController - " + json);

		return directorDto;
	}

	public void updateById(long id, DirectorDto directorDtoUpdate) {
		directorService.updateById(id, directorDtoUpdate);

		final String json = jsonUtils.getJson(directorDtoUpdate);
		log.info("Method to updateById to DirectorController - " + json);
	}

	public void deleteById(long id) {
		directorService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "Director with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		log.info(json);
	}
}
