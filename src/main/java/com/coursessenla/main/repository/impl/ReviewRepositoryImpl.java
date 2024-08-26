package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.repository.ReviewRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		for (int i = 0; i < reviews.size(); i++) {
			Review review = reviews.get(i);
			if (review.getId() == id) {
				reviews.set(i, reviewUpdate);
			}
		}
	}

	@Override
	public void delete(long id) {
		reviews.removeIf(review -> review.getId() == id);
	}
}
