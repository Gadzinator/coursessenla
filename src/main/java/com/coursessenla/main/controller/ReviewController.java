package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReviewController {

	private final ReviewService reviewService;
	private final JsonUtils jsonUtils;

	public void save(ReviewDto reviewDto) {
		log.info("Starting method save with ReviewDto: {}", reviewDto);

		reviewService.save(reviewDto);

		String json = jsonUtils.getJson(reviewDto);
		log.info("Ending method save: {}", json);
	}

	public ReviewDto findById(long id) {
		log.info("Starting method findById with id: {}", id);

		final ReviewDto reviewDto = reviewService.findById(id);

		String json = jsonUtils.getJson(reviewDto);
		log.info("Ending method findById: {}", json);

		return reviewDto;
	}

	public List<ReviewDto> findAll() {
		log.info("Starting method findAll in ReviewController");

		final List<ReviewDto> reviewDtoList = reviewService.findAll();

		final String json = jsonUtils.getJson(reviewDtoList);
		log.info("Ending method findAll in ReviewController {}", json);

		return reviewDtoList;
	}

	public void update(ReviewDto reviewDtoUpdate) {
		log.info("Starting method update with ReviewDto: {}", reviewDtoUpdate);

		reviewService.update(reviewDtoUpdate);

		String json = jsonUtils.getJson(reviewDtoUpdate);
		log.info("Ending method update: {}", json);
	}

	public void deleteById(long id) {
		log.info("Starting method deleteById with id: {}", id);

		reviewService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", String.format("Review with Id %d has been successfully deleted", id));
		String json = jsonUtils.getJson(response);
		log.info("Ending method deleteById. Deletion response: {}", json);
	}
}
