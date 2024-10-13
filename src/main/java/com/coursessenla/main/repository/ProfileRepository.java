package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Profile;

import java.util.Optional;

public interface ProfileRepository {

	Optional<Profile> findByName(String name);
}
