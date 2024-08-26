package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Profile;

import java.util.Optional;

public interface ProfileRepository {
	void save(Profile profile);

	Optional<Profile> findById(long id);

	void update(long id, Profile profileUpdate);

	void deleteById(long id);
}
