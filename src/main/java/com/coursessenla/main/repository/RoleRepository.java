package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Role;

import java.util.Optional;

public interface RoleRepository {
	Optional<Role> findByName(String name);
}
