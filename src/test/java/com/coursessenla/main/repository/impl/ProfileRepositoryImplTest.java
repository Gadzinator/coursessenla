package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.repository.impl.config.H2Config;
import com.coursessenla.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ProfileRepositoryImplTest {

	private static final long PROFILE_ID = 51;
	private static final String PROFILE_NAME = "Alex";

	@Resource
	private ProfileRepositoryImpl profileRepository;

	@Resource
	private UserRepositoryImpl userRepository;

	@Test
	void testSave() {
		final User user = createUser();
		final Profile profile = createProfile(user);
		userRepository.save(user);
		profileRepository.save(profile);

		final Optional<Profile> optionalProfile = profileRepository.findById(PROFILE_ID);
		assertTrue(optionalProfile.isPresent());
		optionalProfile.ifPresent(p -> {
			assertEquals(profile.getFirstName(), p.getFirstName());
			assertEquals(profile.getLastName(), p.getLastName());
			assertEquals(profile.getUser(), p.getUser());
		});
	}

	@Test
	void testFindById() {
		final User user = createUser();
		final Profile profile = createProfile(user);
		userRepository.save(user);
		profileRepository.save(profile);

		final Optional<Profile> optionalProfile = profileRepository.findById(PROFILE_ID);
		assertTrue(optionalProfile.isPresent());
		optionalProfile.ifPresent(p -> {
			assertEquals(profile.getFirstName(), p.getFirstName());
			assertEquals(profile.getLastName(), p.getLastName());
			assertEquals(profile.getUser(), p.getUser());
		});
	}

	@Test
	void testFindAll() {
		final User firstUser = createUser();
		final User secondUser = createUser();
		secondUser.setEmail("usertest@example.com");
		secondUser.setPassword("password");
		final Profile firstProfile = createProfile(firstUser);
		final Profile secondProfile = createProfile(firstUser);
		secondProfile.setFirstName("Bobi");
		secondProfile.setLastName("Djoi");
		secondProfile.setUser(firstUser);
		userRepository.save(firstUser);
		userRepository.save(secondUser);
		profileRepository.save(firstProfile);
		profileRepository.save(secondProfile);

		final List<Profile> profileList = profileRepository.findAll();
		assertNotNull(profileList);
		assertTrue(profileList.contains(firstProfile));
		assertTrue(profileList.contains(secondProfile));
	}

	@Test
	void testUpdate() {
		final User user = createUser();
		final Profile profile = createProfile(user);
		userRepository.save(user);
		profileRepository.save(profile);
		profile.setFirstName(PROFILE_NAME);
		profileRepository.update(profile);

		final Optional<Profile> optionalProfile = profileRepository.findById(PROFILE_ID);
		assertTrue(optionalProfile.isPresent());
		optionalProfile.ifPresent(p ->
				assertEquals(PROFILE_NAME, p.getFirstName()));
	}

	@Test
	void testDeleteById() {
		final User user = createUser();
		final Profile profile = createProfile(user);
		userRepository.save(user);
		profileRepository.save(profile);
		profileRepository.deleteById(PROFILE_ID);

		final Optional<Profile> optionalProfile = profileRepository.findById(PROFILE_ID);
		assertTrue(optionalProfile.isEmpty());
	}

	private Profile createProfile(User user) {
		Profile profile = new Profile();
		profile.setFirstName("John");
		profile.setLastName("Doe");
		profile.setUser(user);
		return profile;
	}

	private User createUser() {
		User user = new User();
		user.setEmail("testuser@example.com");
		user.setPassword("password");

		return user;
	}
}