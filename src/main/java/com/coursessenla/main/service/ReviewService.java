package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.ReviewDto;

import java.util.List;

public interface ReviewService {
	void save(ReviewDto reviewDto);

	ReviewDto findById(long id);

	List<ReviewDto> findAll();

	void update(ReviewDto reviewDtoUpdate);

	void deleteById(long id);
}
