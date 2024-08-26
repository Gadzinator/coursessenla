package com.coursessenla.main.mapper;

import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.entity.Review;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ReviewMapper {

	private final ModelMapper mapper;

	public ReviewDto toDto(Review review) {
		return mapper.map(review, ReviewDto.class);
	}

	public Review toEntity(ReviewDto reviewDto) {
		return mapper.map(reviewDto, Review.class);
	}
}
