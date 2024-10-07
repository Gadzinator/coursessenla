package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.Role;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.exception.UserNotFoundException;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.impl.ProfileRepositoryImpl;
import com.coursessenla.main.repository.impl.RoleRepositoryImpl;
import com.coursessenla.main.repository.impl.UserRepositoryImpl;
import com.coursessenla.main.service.impl.config.ServiceTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
class UserServiceImplTest {

	private static final long USER_ID = 1;
	private final static String USER_EMAIL = "testuser@example.com";
	private static final String USER_PASSWORD = "password";
	private static final String ROLE_USER = "ROLE_USER";
	private static final long ROLE_ID = 1;

	@Mock
	private UserRepositoryImpl userRepository;

	@Mock
	private ProfileRepositoryImpl profileRepository;

	@Mock
	private RoleRepositoryImpl roleRepository;

	@Mock
	private GenericMapperImpl mapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	void createNewUser() {
		final User user = createUser();
		final UserDto userDto = createUserDto();
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		final Role role = createRole();

		when(mapper.mapToEntity(registrationUserDto, User.class)).thenReturn(user);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.of(role));
		doNothing().when(profileRepository).save(any(Profile.class));
		doNothing().when(userRepository).save(user);
		when(mapper.mapToDto(user, UserDto.class)).thenReturn(userDto);

		userService.createNewUser(registrationUserDto);

		verify(mapper).mapToEntity(registrationUserDto, User.class);
		verify(profileRepository).save(any(Profile.class));
		verify(roleRepository).findByName(ROLE_USER);
		verify(userRepository).save(user);
		verify(mapper).mapToDto(user, UserDto.class);
		assertEquals(user.getId(), userDto.getId());
		assertEquals(user.getEmail(), userDto.getEmail());
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final User user = createUser();
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();

		when(mapper.mapToEntity(registrationUserDto, User.class)).thenReturn(user);
		doThrow(new RuntimeException("Database error")).when(userRepository).save(user);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createNewUser(registrationUserDto));

		assertEquals("Database error", exception.getMessage());
		verify(mapper).mapToEntity(registrationUserDto, User.class);
		verify(userRepository).save(user);

	}

	@Test
	void testFindByIdWhenUserExist() {
		final User user = createUser();
		final UserDto userDto = createUserDto();

		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
		when(mapper.mapToDto(user, UserDto.class)).thenReturn(userDto);

		final UserDto existUserDto = userService.findById(USER_ID);

		verify(userRepository).findById(USER_ID);
		verify(mapper).mapToDto(user, UserDto.class);
		assertEquals(userDto, existUserDto);
	}

	@Test
	void testFindByIdWhenUserNotExist() {
		when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.findById(USER_ID));
	}

	@Test
	void testFindByEmailWhenUserExist() {
		final User user = createUser();
		final UserDto userDto = createUserDto();

		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
		when(mapper.mapToDto(user, UserDto.class)).thenReturn(userDto);

		final UserDto actualeUserDto = userService.findByEmail(USER_EMAIL);

		verify(userRepository).findByEmail(USER_EMAIL);
		verify(mapper).mapToDto(user, UserDto.class);
		assertEquals(userDto, actualeUserDto);
	}

	@Test
	void testFindByEmailWhenUserNotExist() {
		when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userService.findByEmail(USER_EMAIL));
	}

	@Test
	void testFindAllWhenListUsersNotEmpty() {
		final User firstUser = createUser();
		final User secondUser = createUser();
		secondUser.setId(2L);
		final UserDto firstUserDto = createUserDto();
		final UserDto secondUserDto = createUserDto();
		secondUserDto.setId(2L);

		final List<User> userList = Arrays.asList(firstUser, secondUser);

		Pageable pageable = PageRequest.of(0, 10);

		Page<User> userPage = new PageImpl<>(userList, pageable, userList.size());

		when(userRepository.findAll(pageable)).thenReturn(userPage);
		when(mapper.mapToDto(firstUser, UserDto.class)).thenReturn(firstUserDto);
		when(mapper.mapToDto(secondUser, UserDto.class)).thenReturn(secondUserDto);

		Page<UserDto> actualUserDtoPage = userService.findAll(pageable);

		verify(userRepository).findAll(pageable);
		verify(mapper).mapToDto(firstUser, UserDto.class);
		verify(mapper).mapToDto(secondUser, UserDto.class);
		assertNotNull(actualUserDtoPage);
		assertEquals(2, actualUserDtoPage.getContent().size());
		assertTrue(actualUserDtoPage.getContent().contains(firstUserDto));
		assertTrue(actualUserDtoPage.getContent().contains(secondUserDto));
	}

	@Test
	void testFindAllWhenListUsersEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(userRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(UserNotFoundException.class, () -> userService.findAll(pageable));
	}

	@Test
	void testDeleteByIdWhenUserExist() {
		final User user = createUser();

		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
		doNothing().when(userRepository).deleteById(USER_ID);

		userService.deleteById(USER_ID);

		verify(userRepository).findById(USER_ID);
		verify(userRepository).deleteById(USER_ID);
	}

	@Test
	void testDeleteByIdWhenUserNotExist() {
		when(userRepository.findById(USER_ID)).thenThrow(UserNotFoundException.class);

		assertThrows(UserNotFoundException.class, () -> userService.deleteById(USER_ID));
		verify(userRepository, never()).deleteById(USER_ID);
	}

	private User createUser() {
		User user = new User();
		user.setId(USER_ID);
		user.setEmail(USER_EMAIL);
		user.setPassword(USER_PASSWORD);

		return user;
	}

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(USER_ID);
		userDto.setEmail(USER_EMAIL);

		return userDto;
	}

	private RegistrationUserDto createRegistrationUserDto() {
		RegistrationUserDto registrationUserDto = new RegistrationUserDto();
		registrationUserDto.setId(USER_ID);
		registrationUserDto.setFirstName("Pushkin");
		registrationUserDto.setLastName("Aleksandr");
		registrationUserDto.setPassword(USER_PASSWORD);
		registrationUserDto.setConfirmPassword(USER_PASSWORD);
		registrationUserDto.setEmail(USER_EMAIL);

		return registrationUserDto;
	}

	private Role createRole() {
		Role role = new Role();
		role.setId(ROLE_ID);
		role.setName(ROLE_USER);

		return role;
	}
}
