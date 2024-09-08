package com.coursessenla.main.service.impl;

import com.coursessenla.main.annotation.Transaction;
import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.ReviewRepository;
import com.coursessenla.main.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final GenericMapper mapper;

	@Transaction
	@Override
	public void save(ReviewDto reviewDto) {
		reviewRepository.save(mapper.mapToDto(reviewDto, Review.class));
	}

	@Override
	public ReviewDto findById(long id) {
		return reviewRepository.findById(id)
				.map(review -> mapper.mapToEntity(review, ReviewDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Review with id %d was not found", id)));
	}

	@Transaction
	@Override
	public void updateById(long id, ReviewDto reviewDtoUpdate) {
		findById(id);
		reviewRepository.updateById(id, mapper.mapToDto(reviewDtoUpdate, Review.class));
	}

	@Transaction
	@Override
	public void deleteById(long id) {
		findById(id);
		reviewRepository.delete(id);
	}
}
