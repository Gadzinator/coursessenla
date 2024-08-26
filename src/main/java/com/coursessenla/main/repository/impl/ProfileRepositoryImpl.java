package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.repository.ProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProfileRepositoryImpl implements ProfileRepository {

	private final List<Profile> profiles = new ArrayList<>();

	@Override
	public void save(Profile profile) {
		profiles.add(profile);
	}

	@Override
	public Optional<Profile> findById(long id) {
		return profiles.stream()
				.filter(profile -> profile.getId() == id)
				.findFirst();
	}

	@Override
	public void update(long id, Profile profileUpdate) {
		for (int i = 0; i < profiles.size(); i++) {
			final Profile profile = profiles.get(i);
			if (profile.getId() == id) {
				profiles.set(i, profileUpdate);
			}
		}
	}

	@Override
	public void deleteById(long id) {
		profiles.removeIf(profile -> profile.getId() == id);
	}
}
