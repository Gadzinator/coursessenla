package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Controller
public class ReviewController {

	private final ReviewService reviewService;
	private final JsonUtils jsonUtils;

	public void save(ReviewDto reviewDto) {
		reviewService.save(reviewDto);

		String json = jsonUtils.getJson(reviewDto);
		log.info("Method to save to ReviewController - " + json);
	}

	public ReviewDto findById(long id) {
		final ReviewDto reviewDto = reviewService.findById(id);

		String json = jsonUtils.getJson(reviewDto);
		log.info("Method to findById to ReviewController - " + json);

		return reviewDto;
	}

	public void updateById(long id, ReviewDto reviewDtoUpdate) {
		reviewService.updateById(id, reviewDtoUpdate);

		String json = jsonUtils.getJson(reviewDtoUpdate);
		log.info("Method to updateById to ReviewController - " + json);
	}

	public void deleteById(long id) {
		reviewService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "Review with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		log.info(json);
	}
}
