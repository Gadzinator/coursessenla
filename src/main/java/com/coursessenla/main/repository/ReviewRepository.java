package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Review;

import java.util.Optional;

public interface ReviewRepository {
	void save(Review review);

	Optional<Review> findById(long id);

	void updateById(long id, Review reviewUpdate);

	void delete(long id);
}
