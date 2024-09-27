package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.exception.ReviewNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.ReviewRepositoryImpl;
import com.coursessenla.main.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepositoryImpl reviewRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public void save(ReviewDto reviewDto) {
		log.info("Starting method save: {}", reviewDto);
		reviewRepository.save(mapper.mapToEntity(reviewDto, Review.class));
		log.info("Ending method save: {}", reviewDto);
	}

	@Transactional
	@Override
	public ReviewDto findById(long id) {
		log.info("Starting method findById: {}", id);
		final ReviewDto reviewDto = reviewRepository.findById(id)
				.map(review -> mapper.mapToDto(review, ReviewDto.class))
				.orElseThrow(() -> new ReviewNotFoundException(String.format("Review with id %d was not found", id)));
		log.info("Ending method findById: {}", reviewDto);

		return reviewDto;
	}

	@Transactional
	@Override
	public Page<ReviewDto> findAll(Pageable pageable) {
		log.info("Starting method findAll");
		final Page<ReviewDto> reviewDtoPage = reviewRepository.findAll(pageable)
				.map(review -> mapper.mapToDto(review, ReviewDto.class));

		if (reviewDtoPage.isEmpty()) {
			log.warn("No reviews were found, throwing ReviewNotFoundException");
			throw new ReviewNotFoundException("No reviews were found");
		}

		log.info("Ending method findAll: {}", reviewDtoPage);

		return reviewDtoPage;
	}

	@Transactional
	@Override
	public void update(ReviewDto reviewDtoUpdate) {
		log.info("Starting method update: {}", reviewDtoUpdate);
		findById(reviewDtoUpdate.getId());
		reviewRepository.update(mapper.mapToEntity(reviewDtoUpdate, Review.class));
		log.info("Ending method update: {}", reviewDtoUpdate);
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting method deleteById: {}", id);
		findById(id);
		reviewRepository.deleteById(id);
		log.info("Ending method deleteById: {}", id);
	}
}
