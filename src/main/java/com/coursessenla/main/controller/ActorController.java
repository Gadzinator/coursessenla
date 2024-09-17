package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.service.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ActorController {

	private final ActorService actorService;
	private final JsonUtils jsonUtils;

	public void save(ActorDto actorDto) {
		log.info("Starting method save");

		actorService.save(actorDto);

		final String json = jsonUtils.getJson(actorDto);
		log.info("Ending method save: {}", json);
	}

	public ActorDto findById(long id) {
		log.info("Starting method findById with id: {}", id);

		final ActorDto actorDto = actorService.findById(id);

		final String json = jsonUtils.getJson(actorDto);
		log.info("Ending method findById {}", json);

		return actorDto;
	}

	public ActorDto findByName(String name) {
		log.info("Starting method findByName with name: {}", name);

		final ActorDto actorDto = actorService.findByName(name);

		final String json = jsonUtils.getJson(actorDto);
		log.info("Ending method findByName: {}", json);

		return actorDto;
	}

	public List<ActorDto> findAll() {
		log.info("Starting method findAll");

		final List<ActorDto> actorDtoList = actorService.findAll();

		final String json = jsonUtils.getJson(actorDtoList);
		log.info("Ending method findAll: {}", json);

		return actorDtoList;
	}

	public void update(ActorDto actorDtoUpdate) {
		log.info("Starting method update with actorDtoUpdate: {}", actorDtoUpdate);

		actorService.update(actorDtoUpdate);

		final String json = jsonUtils.getJson(actorDtoUpdate);
		log.info("Ending method update {}", json);
	}

	public void deleteById(long id) {
		log.info("Starting method delete in with id: {}", id);

		actorService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", String.format("Actor with Id %d has been successfully deleted", id));
		String json = jsonUtils.getJson(response);
		log.info("Ending method delete. Deletion response: {}", json);
	}
}
