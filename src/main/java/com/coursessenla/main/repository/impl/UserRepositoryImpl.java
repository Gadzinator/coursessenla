package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Role;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private final List<User> users = new ArrayList<>();

	@Override
	public void save(User user) {
		user.setRole(Role.ROLE_USER);
		users.add(user);
	}

	@Override
	public Optional<User> findById(long id) {
		return users.stream()
				.filter(user -> user.getId() == id)
				.findFirst();
	}

	@Override
	public void deleteById(long id) {
		users.removeIf(user -> user.getId() == id);
	}
}
