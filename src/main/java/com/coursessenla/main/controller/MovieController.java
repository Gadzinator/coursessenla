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
		movieService.save(movieDto);

		final String json = jsonUtils.getJson(movieDto);
		log.info("Method to save to MovieController - " + json);
	}

	public MovieDto findById(long id) {
		final MovieDto movieDto = movieService.findById(id);

		final String json = jsonUtils.getJson(movieDto);
		log.info("Method to findById to MovieController - " + json);

		return movieDto;
	}

	public List<MovieDto> findByName(String genreName) {
		final List<MovieDto> movieDtoList = movieService.findByGenre(genreName);

		final String json = jsonUtils.getJson(movieDtoList);
		log.info("Method to findByName to MovieController - " + json);

		return movieDtoList;
	}

	public void updateById(long id, MovieDto movieDtoUpdate) {
		movieService.updateById(id, movieDtoUpdate);

		final String json = jsonUtils.getJson(movieDtoUpdate);
		log.info("Method to updateById to MovieController - " + json);
	}

	public void deleteById(long id) {
		movieService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "Movie with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		log.info(json);
	}
}
