package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.service.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ActorController {

	private final ActorService actorService;
	private final JsonUtils jsonUtils;

	public void save(ActorDto actorDto) {
		actorService.save(actorDto);

		final String json = jsonUtils.getJson(actorDto);
		log.info("Method to save to ActorController - {}", json);
	}

	public ActorDto findById(long id) {
		final ActorDto actorDto = actorService.findById(id);

		final String json = jsonUtils.getJson(actorDto);
		log.info("Method to findById to ActorController - " + json);

		return actorDto;
	}

	public ActorDto findByName(String name) {
		final ActorDto actorDto = actorService.findByName(name);

		final String json = jsonUtils.getJson(actorDto);
		log.info("Method to findByName to ActorController - " + json);

		return actorDto;
	}

	public void updateById(long id, ActorDto actorDtoUpdate) {
		actorService.update(id, actorDtoUpdate);

		final String json = jsonUtils.getJson(actorDtoUpdate);
		log.info("Method to updateById to ActorController - " + json);
	}

	public void delete(long id) {
		actorService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "Actor with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		log.info(json);
	}
}
