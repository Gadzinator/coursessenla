package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.service.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
public class GenreController {

	private final GenreService genreService;
	private final JsonUtils jsonUtils;

	public void save(GenreDto genreDto) {
		genreService.save(genreDto);

		final String json = jsonUtils.getJson(genreDto);
		System.out.println("Method to save to GenreController - " + json);
	}

	public GenreDto findById(long id) {
		final GenreDto genreDto = genreService.findById(id);

		final String json = jsonUtils.getJson(genreDto);
		System.out.println("Method to findById to GenreController - " + json);

		return genreDto;
	}

	public GenreDto findByName(String name) {
		final GenreDto genreDto = genreService.findByName(name);

		final String json = jsonUtils.getJson(genreDto);
		System.out.println("Method to findByName to GenreController - " + json);

		return genreDto;
	}

	public void updateById(long id, GenreDto genreDtoUpdate) {
		genreService.updateById(id, genreDtoUpdate);

		final String json = jsonUtils.getJson(genreDtoUpdate);
		System.out.println("Method to updateById to GenreController - " + json);
	}

	public List<GenreDto> findAllByName(List<String> genreNames) {
		final List<GenreDto> genreDtoList = genreService.findAllByNames(genreNames);

		final String json = jsonUtils.getJson(genreDtoList);
		System.out.println("Method to findAllByName to GenreController - " + json);

		return genreDtoList;
	}

	public void deleteById(long id) {
		genreService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "Genre with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		System.out.println(json);
	}
}
