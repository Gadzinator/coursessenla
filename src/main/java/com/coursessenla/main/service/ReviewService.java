package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
	void save(ReviewDto reviewDto);

	ReviewDto findById(long id);

	Page<ReviewDto> findAll(Pageable pageable);

	void update(ReviewDto reviewDtoUpdate);

	void deleteById(long id);
}
