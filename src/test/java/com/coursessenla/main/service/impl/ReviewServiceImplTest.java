package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.exception.ReviewNotFoundException;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.impl.ReviewRepositoryImpl;
import com.coursessenla.main.service.impl.config.ServiceTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
class ReviewServiceImplTest {

	private static final long REVIEW_ID = 1;
	private static final String UPDATE_PLAYLIST_CONTENT = "Good movie";

	@Mock
	private ReviewRepositoryImpl reviewRepository;

	@Mock
	private GenericMapperImpl mapper;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	@Test
	void save() {
		final Review review = createReview();
		final ReviewDto reviewDto = createReviewDto();

		when(mapper.mapToEntity(reviewDto, Review.class)).thenReturn(review);
		doNothing().when(reviewRepository).save(review);

		reviewService.save(reviewDto);

		verify(mapper).mapToEntity(reviewDto, Review.class);
		verify(reviewRepository).save(review);
		assertEquals(review.getContent(), reviewDto.getContent());
		assertEquals(review.getRating(), reviewDto.getRating());
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final Review review = createReview();
		final ReviewDto reviewDto = createReviewDto();

		when(mapper.mapToEntity(reviewDto, Review.class)).thenReturn(review);
		doThrow(new RuntimeException("Database error")).when(reviewRepository).save(review);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> reviewService.save(reviewDto));

		assertEquals("Database error", exception.getMessage());
		verify(mapper).mapToEntity(reviewDto, Review.class);
		verify(reviewRepository).save(review);
	}

	@Test
	void testFindByIdWhenReviewExist() {
		final Review review = createReview();
		final ReviewDto reviewDto = createReviewDto();

		when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(review));
		when(mapper.mapToDto(review, ReviewDto.class)).thenReturn(reviewDto);

		final ReviewDto existReviewDto = reviewService.findById(REVIEW_ID);

		verify(reviewRepository).findById(REVIEW_ID);
		verify(mapper).mapToDto(review, ReviewDto.class);
		assertEquals(reviewDto, existReviewDto);
	}

	@Test
	void testFindByIdWhenReviewNotExist() {
		when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.empty());

		assertThrows(ReviewNotFoundException.class, () -> reviewService.findById(REVIEW_ID));
	}

	@Test
	void testFindAllWhenListReviewsNotEmpty() {
		final Review firstReview = createReview();
		final Review secondReview = createReview();
		secondReview.setId(2L);
		final ReviewDto firstReviewDto = createReviewDto();
		final ReviewDto secondReviewDto = createReviewDto();
		secondReviewDto.setId(2L);

		final List<Review> reviewList = Arrays.asList(firstReview, secondReview);

		Pageable pageable = PageRequest.of(0, 10);

		Page<Review> reviewPage = new PageImpl<>(reviewList, pageable, reviewList.size());

		when(reviewRepository.findAll(pageable)).thenReturn(reviewPage);
		when(mapper.mapToDto(firstReview, ReviewDto.class)).thenReturn(firstReviewDto);
		when(mapper.mapToDto(secondReview, ReviewDto.class)).thenReturn(secondReviewDto);

		Page<ReviewDto> actualReviewDtoPage = reviewService.findAll(pageable);

		verify(reviewRepository).findAll(pageable);
		verify(mapper).mapToDto(firstReview, ReviewDto.class);
		verify(mapper).mapToDto(secondReview, ReviewDto.class);
		assertNotNull(actualReviewDtoPage);
		assertEquals(2, actualReviewDtoPage.getContent().size());
		assertTrue(actualReviewDtoPage.getContent().contains(firstReviewDto));
		assertTrue(actualReviewDtoPage.getContent().contains(secondReviewDto));
	}

	@Test
	void testFindAllWhenListReviewsEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(reviewRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(ReviewNotFoundException.class, () -> reviewService.findAll(pageable));
	}

	@Test
	void testUpdateWhenReviewFound() {
		final Review review = createReview();
		final ReviewDto reviewDto = createReviewDto();
		reviewDto.setContent(UPDATE_PLAYLIST_CONTENT);

		when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(review));
		when(mapper.mapToDto(any(Review.class), eq(ReviewDto.class))).thenReturn(reviewDto);
		when(mapper.mapToEntity(any(ReviewDto.class), eq(Review.class))).thenAnswer(invocation -> {
			ReviewDto dto = invocation.getArgument(0);
			review.setContent(dto.getContent());
			return review;
		});
		doNothing().when(reviewRepository).update(any(Review.class));

		reviewService.update(reviewDto);

		final ArgumentCaptor<Review> reviewArgumentCaptor = ArgumentCaptor.forClass(Review.class);
		verify(reviewRepository).findById(REVIEW_ID);
		verify(mapper).mapToDto(any(Review.class), eq(ReviewDto.class));
		verify(mapper).mapToEntity(any(ReviewDto.class), eq(Review.class));
		verify(reviewRepository).update(reviewArgumentCaptor.capture());
		final Review savedReview = reviewArgumentCaptor.getValue();
		assertEquals(UPDATE_PLAYLIST_CONTENT, savedReview.getContent());
	}

	@Test
	void testUpdateWhenReviewNotFound() {
		final ReviewDto reviewDto = createReviewDto();

		when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.empty());

		assertThrows(ReviewNotFoundException.class, () -> reviewService.update(reviewDto));
		verify(reviewRepository).findById(REVIEW_ID);
		verify(reviewRepository, never()).update(any(Review.class));
	}

	@Test
	void testDeleteByIdWhenReviewExist() {
		final Review review = createReview();
		final ReviewDto reviewDto = createReviewDto();

		when(reviewRepository.findById(REVIEW_ID)).thenReturn(Optional.of(review));
		when(mapper.mapToDto(any(Review.class), eq(ReviewDto.class))).thenReturn(reviewDto);
		doNothing().when(reviewRepository).deleteById(REVIEW_ID);

		reviewService.deleteById(REVIEW_ID);

		verify(reviewRepository).findById(REVIEW_ID);
		verify(mapper).mapToDto(any(Review.class), eq(ReviewDto.class));
		verify(reviewRepository).deleteById(REVIEW_ID);
	}

	@Test
	void testDeleteByIdWhenReviewNotExist() {
		when(reviewRepository.findById(REVIEW_ID)).thenThrow(ReviewNotFoundException.class);

		assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteById(REVIEW_ID));
		verify(reviewRepository, never()).deleteById(REVIEW_ID);
	}

	private Review createReview() {
		Review review = new Review();
		review.setId(REVIEW_ID);
		review.setContent("This is a great movie!");
		review.setRating(8.5);

		return review;
	}

	private ReviewDto createReviewDto() {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setId(REVIEW_ID);
		reviewDto.setContent("This is a great movie!");
		reviewDto.setRating(8.5);

		return reviewDto;
	}
}
