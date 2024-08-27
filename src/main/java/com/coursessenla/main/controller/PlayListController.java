package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.service.PlayListService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Controller
public class PlayListController {

	private PlayListService playListService;
	private JsonUtils jsonUtils;

	public void save(PlayListDto playListDto) {
		playListService.save(playListDto);

		final String json = jsonUtils.getJson(playListDto);
		log.info("Method to save to PlaylistController - " + json);
	}

	public PlayListDto findById(long id) {
		final PlayListDto playListDto = playListService.findById(id);

		final String json = jsonUtils.getJson(playListDto);
		log.info("Method to findById to PlaylistController - " + json);

		return playListDto;
	}

	public void updateById(long id, PlayListDto playListDtoUpdate) {
		playListService.updateById(id, playListDtoUpdate);

		final String json = jsonUtils.getJson(playListDtoUpdate);
		log.info("Method to updateById to PlaylistController - " + json);
	}

	public void deleteById(long id) {
		playListService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "Playlist with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		log.info(json);
	}
}
