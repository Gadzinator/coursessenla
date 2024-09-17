package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class GenreController {

	private final GenreService genreService;
	private final JsonUtils jsonUtils;

	public void save(GenreDto genreDto) {
		log.info("Starting method save with GenreDto: {}", genreDto);

		genreService.save(genreDto);

		final String json = jsonUtils.getJson(genreDto);
		log.info("Ending method save: {}", json);
	}

	public GenreDto findById(long id) {
		log.info("Starting method findById with id: {}", id);

		final GenreDto genreDto = genreService.findById(id);

		final String json = jsonUtils.getJson(genreDto);
		log.info("Ending method findById: {}", json);

		return genreDto;
	}

	public GenreDto findByName(String name) {
		log.info("Starting method findByName with name: {}", name);

		final GenreDto genreDto = genreService.findByName(name);

		final String json = jsonUtils.getJson(genreDto);
		log.info("Ending method findByName: {}", json);

		return genreDto;
	}

	public void update(GenreDto genreDtoUpdate) {
		log.info("Starting method update with GenreDto: {}", genreDtoUpdate);

		genreService.update(genreDtoUpdate);

		final String json = jsonUtils.getJson(genreDtoUpdate);
		log.info("Ending method update: {}", json);
	}

	public List<GenreDto> findAllByName(List<String> genreNames) {
		log.info("Starting method findAllByName with genreNames: {}", genreNames);

		final List<GenreDto> genreDtoList = genreService.findAllByNames(genreNames);

		final String json = jsonUtils.getJson(genreDtoList);
		log.info("Ending method findAllByName: {}", json);

		return genreDtoList;
	}

	public List<GenreDto> findAll() {
		log.info("Starting method findAll");

		final List<GenreDto> genreDtoList = genreService.findAll();

			final String json = jsonUtils.getJson(genreDtoList);
			log.info("Ending method findAll: {}", json);

		return genreDtoList;
	}

	public void deleteById(long id) {
		log.info("Starting method deleteById with id: {}", id);

		genreService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", String.format("Genre with Id %d has been successfully deleted", id));
		String json = jsonUtils.getJson(response);
		log.info("Ending method deleteById. Deletion response: {}", json);
	}
}
