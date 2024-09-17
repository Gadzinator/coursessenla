package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MovieController {

	private final MovieService movieService;
	private final JsonUtils jsonUtils;

	public void save(MovieDto movieDto) {
		log.info("Starting method save with MovieDto: {}", movieDto);

		movieService.save(movieDto);

		final String json = jsonUtils.getJson(movieDto);
		log.info("Ending method save: {}", json);
	}

	public MovieDto findById(long id) {
		log.info("Starting method findById with id: {}", id);

		final MovieDto movieDto = movieService.findById(id);

		final String json = jsonUtils.getJson(movieDto);
		log.info("Ending method findById: {}", json);

		return movieDto;
	}

	public List<MovieDto> findByName(String genreName) {
		log.info("Starting method findByName with genreName: {}", genreName);

		final List<MovieDto> movieDtoList = movieService.findByGenre(genreName);

		final String json = jsonUtils.getJson(movieDtoList);
		log.info("Ending method findByName: {}", json);

		return movieDtoList;
	}

	public List<MovieDto> findAll() {
		log.info("Starting method findAll");

		final List<MovieDto> movieDtoList = movieService.findAll();

		final String json = jsonUtils.getJson(movieDtoList);
		log.info("Ending method findAll: {}", json);

		return movieDtoList;
	}

	public void update(MovieDto movieDtoUpdate) {
		log.info("Starting method update with MovieDto: {}", movieDtoUpdate);

		movieService.update(movieDtoUpdate);

		final String json = jsonUtils.getJson(movieDtoUpdate);
		log.info("Ending method update: {}", json);
	}

	public void deleteById(long id) {
		log.info("Starting method deleteById with id: {}", id);

		movieService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", String.format("Movie with Id %d has been successfully deleted", id));
		String json = jsonUtils.getJson(response);
		log.info("Ending method deleteById. Deletion response: {}", json);
	}
}
