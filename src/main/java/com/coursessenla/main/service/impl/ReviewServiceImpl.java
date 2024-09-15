package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.ReviewRepositoryImpl;
import com.coursessenla.main.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepositoryImpl reviewRepository;
	private final GenericMapper mapper;

	@Transactional
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

	@Override
	public List<ReviewDto> findAll() {
		return reviewRepository.findAll().stream()
				.map(review -> mapper.mapToDto(review, ReviewDto.class))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void update(ReviewDto reviewDtoUpdate) {
		findById(reviewDtoUpdate.getId());
		reviewRepository.update(mapper.mapToDto(reviewDtoUpdate, Review.class));
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		findById(id);
		reviewRepository.deleteById(id);
	}
}
