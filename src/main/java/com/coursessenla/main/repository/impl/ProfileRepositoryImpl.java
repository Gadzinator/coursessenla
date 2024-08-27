package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.repository.ProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

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
		final OptionalInt indexOptional = IntStream.range(0, profiles.size())
				.filter(i -> profiles.get(i).getId() == id)
				.findFirst();

		indexOptional.ifPresent(index -> profiles.set(index, profileUpdate));
	}

	@Override
	public void deleteById(long id) {
		profiles.removeIf(profile -> profile.getId() == id);
	}
}
