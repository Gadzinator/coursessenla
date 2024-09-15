package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Review;
import com.coursessenla.main.repository.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepositoryImpl extends AbstractDao<Review, Long> {

	protected ReviewRepositoryImpl() {
		super(Review.class);
	}
}
