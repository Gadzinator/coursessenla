package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.ReviewDto;

public interface ReviewService {
	void save(ReviewDto reviewDto);

	ReviewDto findById(long id);

	void updateById(long id, ReviewDto reviewDtoUpdate);

	void deleteById(long id);
}
