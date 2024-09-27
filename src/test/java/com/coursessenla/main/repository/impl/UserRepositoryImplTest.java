package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.repository.impl.config.H2Config;
import com.coursessenla.main.repository.impl.config.LiquibaseConfigTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {H2Config.class, LiquibaseConfigTest.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class UserRepositoryImplTest {

	private final static long USER_ID = 51;
	private final static String USER_EMAIL = "testuser@example.com";
	private final static String UPDATE_USER_EMAIL = "usertest@example.com";

	@Resource
	private UserRepositoryImpl userRepository;

	@Test
	void testSave() {
		final User user = createUser();
		userRepository.save(user);

		final Optional<User> optionalUser = userRepository.findById(USER_ID);
		assertTrue(optionalUser.isPresent());
		optionalUser.ifPresent((User u) -> assertEquals(USER_EMAIL, u.getEmail()));
	}

	@Test
	void testFindById() {
		final User user = createUser();
		userRepository.save(user);

		final Optional<User> optionalUser = userRepository.findById(USER_ID);
		assertTrue(optionalUser.isPresent());
		optionalUser.ifPresent(u -> assertEquals(USER_EMAIL, u.getEmail()));
	}

	@Test
	void findByAll() {
		final User firstUser = createUser();
		final User secendtUser = createUser();
		userRepository.save(firstUser);
		userRepository.save(secendtUser);

		Pageable pageable = PageRequest.of(0, 10);

		final Page<User> userPage = userRepository.findAll(pageable);

		assertNotNull(userPage);
		assertEquals(10, userPage.getContent().size());
		assertEquals(52, userPage.getTotalElements());
	}

	@Test
	void testFindByEmail() {
		final User user = createUser();
		userRepository.save(user);

		final Optional<User> optionalUser = userRepository.findByEmail(USER_EMAIL);
		assertTrue(optionalUser.isPresent());
		optionalUser.ifPresent(u -> {
			assertEquals(USER_ID, u.getId());
			assertEquals(USER_EMAIL, u.getEmail());
		});
	}

	@Test
	void testUpdate() {
		final User user = createUser();
		userRepository.save(user);
		user.setEmail(UPDATE_USER_EMAIL);
		userRepository.update(user);

		final Optional<User> optionalUser = userRepository.findById(USER_ID);
		assertTrue(optionalUser.isPresent());
		optionalUser.ifPresent(u ->
				assertEquals(user.getEmail(), u.getEmail()));
	}

	@Test
	void testDelete() {
		final User user = createUser();
		userRepository.save(user);

		userRepository.deleteById(USER_ID);

		final Optional<User> optionalUser = userRepository.findById(USER_ID);
		assertTrue(optionalUser.isEmpty());
	}

	private User createUser() {
		User user = new User();
		user.setEmail(USER_EMAIL);
		user.setPassword("password");

		return user;
	}
}
