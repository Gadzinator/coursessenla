package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.ReviewRepository;
import com.coursessenla.main.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final GenericMapperImpl<Review, ReviewDto> mapper;

	@Override
	public void save(ReviewDto reviewDto) {
		reviewRepository.save(mapper.toEntity(reviewDto));
	}

	@Override
	public ReviewDto findById(long id) {
		return reviewRepository.findById(id)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Review with id %d was not found", id)));
	}

	@Override
	public void updateById(long id, ReviewDto reviewDtoUpdate) {
		findById(id);
		reviewRepository.updateById(id, mapper.toEntity(reviewDtoUpdate));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		reviewRepository.delete(id);
	}
}
