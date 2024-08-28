package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

	private final List<Review> reviews = new ArrayList<>();

	@Override
	public void save(Review review) {
		reviews.add(review);
	}

	@Override
	public Optional<Review> findById(long id) {
		return reviews.stream()
				.filter(review -> review.getId() == id)
				.findFirst();
	}

	@Override
	public void updateById(long id, Review reviewUpdate) {
		final OptionalInt indexOptional = IntStream.range(0, reviews.size())
				.filter(i -> reviews.get(i).getId() == id)
				.findFirst();

		indexOptional.ifPresent(index -> reviews.set(index, reviewUpdate));
	}

	@Override
	public void delete(long id) {
		reviews.removeIf(review -> review.getId() == id);
	}
}
