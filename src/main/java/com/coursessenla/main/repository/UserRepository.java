package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
	void save(User user);

	Optional<User> findById(long id);

	void deleteById(long id);
}
