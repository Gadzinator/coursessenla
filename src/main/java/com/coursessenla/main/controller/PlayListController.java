package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.service.PlayListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PlayListController {

	private final PlayListService playListService;
	private final JsonUtils jsonUtils;

	public void save(PlayListDto playListDto) {
		log.info("Starting method save with PlayListDto: {}", playListDto);

		playListService.save(playListDto);

		final String json = jsonUtils.getJson(playListDto);
		log.info("Ending method save: {}", json);
	}

	public PlayListDto findById(long id) {
		log.info("Starting method findById with id: {}", id);

		final PlayListDto playListDto = playListService.findById(id);

		final String json = jsonUtils.getJson(playListDto);
		log.info("Ending method findById: {}", json);

		return playListDto;
	}

	public List<PlayListDto> findAll() {
		log.info("Starting method findAll");

		final List<PlayListDto> playListDtoList = playListService.findAll();

		final String json = jsonUtils.getJson(playListDtoList);
		log.info("Ending method findAll: {}", json);

		return playListDtoList;
	}

	public void update(PlayListDto playListDtoUpdate) {
		log.info("Starting method update with PlayListDto: {}", playListDtoUpdate);

		playListService.update(playListDtoUpdate);

		final String json = jsonUtils.getJson(playListDtoUpdate);
		log.info("Ending method update: {}", json);
	}

	public void deleteById(long id) {
		log.info("Starting method deleteById with id: {}", id);

		playListService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", String.format("Playlist with Id %d has been successfully deleted", id));
		String json = jsonUtils.getJson(response);
		log.info("Ending method deleteById. Deletion response: {}", json);
	}
}
