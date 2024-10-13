package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.JwtRequest;
import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.Role;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.exception.AuthenticationFailedException;
import com.coursessenla.main.repository.impl.ProfileRepositoryImpl;
import com.coursessenla.main.service.impl.config.ServiceTestConfiguration;
import com.coursessenla.main.utils.JwtTokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
class AuthServiceImplTest {

	private static final String USER_FIRST_NAME = "Alex";
	private static final String USER_LAST_NAME = "Petrov";
	private static final String USER_EMAIL = "alex@gmail.ru";
	private static final String PASSWORD = "Alex";
	private static final String TOKEN = "tokenString";

	@Mock
	private ProfileRepositoryImpl profileRepository;

	@Mock
	private JwtTokenUtils jwtTokenUtils;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AuthServiceImpl authService;

	@Test
	void testCreateAuthTokenThrowsUsernameNotFoundExceptionWhenUsernameIsInvalid() {
		JwtRequest authRequest = createJwtRequest();

		when(profileRepository.findByName(USER_FIRST_NAME)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> authService.createAuthToken(authRequest));
	}

	@Test
	void testCreateAuthTokenThrowsExceptionForInvalidPassword() {
		JwtRequest authRequest = createJwtRequest();
		final Profile profile = createProfile();
		User user = createUser();
		final UserDetails userDetails = createUserDetails(profile, user);

		when(profileRepository.findByName(USER_FIRST_NAME)).thenReturn(Optional.of(profile));
		when(passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())).thenReturn(false);

		assertThrows(AuthenticationFailedException.class, () -> authService.createAuthToken(authRequest));
		verify(profileRepository).findByName(USER_FIRST_NAME);
		verify(passwordEncoder).matches(authRequest.getPassword(), userDetails.getPassword());
	}

	@Test
	void testCreateAuthTokenValidCredentials() {
		JwtRequest authRequest = createJwtRequest();
		final Profile profile = createProfile();
		User user = createUser();
		final UserDetails userDetails = createUserDetails(profile, user);

		when(profileRepository.findByName(USER_FIRST_NAME)).thenReturn(Optional.of(profile));
		when(jwtTokenUtils.generateToken(userDetails)).thenReturn(TOKEN);
		when(passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())).thenReturn(true);

		String token = authService.createAuthToken(authRequest);

		verify(profileRepository).findByName(USER_FIRST_NAME);
		verify(jwtTokenUtils).generateToken(userDetails);
		verify(passwordEncoder).matches(authRequest.getPassword(), userDetails.getPassword());
		assertEquals(TOKEN, token);
	}

	@Test
	void loadUserByUsernameWhenUserNotExist() {
		when(profileRepository.findByName(USER_FIRST_NAME)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername(USER_FIRST_NAME));

		verify(profileRepository).findByName(USER_FIRST_NAME);
	}

	@Test
	void loadUserByUsernameWhenUserExist() {
		User user = createUser();
		final Profile profile = createProfile();

		when(profileRepository.findByName(USER_FIRST_NAME)).thenReturn(Optional.of(profile));

		UserDetails userDetails = authService.loadUserByUsername(USER_FIRST_NAME);

		verify(profileRepository).findByName(USER_FIRST_NAME);
		assertEquals(userDetails.getUsername(), profile.getFirstName());
		assertEquals(userDetails.getPassword(), user.getPassword());
	}

	private JwtRequest createJwtRequest() {
		JwtRequest authRequest = new JwtRequest();
		authRequest.setUsername(USER_FIRST_NAME);
		authRequest.setPassword(PASSWORD);

		return authRequest;
	}

	private UserDetails createUserDetails(Profile profile, User user) {

		return new org.springframework.security.core.userdetails.User(
				profile.getFirstName(),
				user.getPassword(),
				mapRolesToAuthorities(user.getRoles())
		);
	}

	private Collection<GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	private Profile createProfile() {
		Profile profile = new Profile();
		profile.setFirstName(USER_FIRST_NAME);
		profile.setLastName(USER_LAST_NAME);
		profile.setUser(createUser());

		return profile;
	}

	private User createUser() {
		List<Role> roles = new ArrayList<>();
		final Role role = new Role("ROLE_SUER");
		roles.add(role);
		User user = new User();
		user.setEmail(USER_EMAIL);
		user.setPassword(PASSWORD);
		user.setRoles(roles);

		return user;
	}
}
