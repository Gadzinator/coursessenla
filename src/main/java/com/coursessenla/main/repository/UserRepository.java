package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.User;

import java.util.Optional;

public interface UserRepository {

	Optional<User> findByEmail(String name);
}
