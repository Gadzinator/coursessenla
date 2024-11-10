package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.JwtRequest;
import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.Role;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.exception.AuthenticationFailedException;
import com.coursessenla.main.repository.impl.ProfileRepositoryImpl;
import com.coursessenla.main.service.AuthService;
import com.coursessenla.main.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final JwtTokenUtils jwtTokenUtils;
	private final ProfileRepositoryImpl profileRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public String  createAuthToken(JwtRequest authRequest) {
		log.info("Starting method createAuthToken: {}", authRequest);
		UserDetails userDetails = loadUserByUsername(authRequest.getUsername());
		if (!passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword())) {
			log.error("Authentication failed password.");
			throw new AuthenticationFailedException("Password not valid");
		}

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, authRequest.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final boolean authenticated = authentication.isAuthenticated();
		if (!authenticated) {
			log.error("Authentication failed for user: {}", authRequest.getUsername());
			throw new AuthenticationFailedException("Authentication failed");
		}

		final String token = jwtTokenUtils.generateToken(userDetails);
		log.info("Ending method createAuthToken: {}", token);

		return token;
	}

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) {
		log.info("Starting method loadUserByUsername: {}", username);

		final Profile profile = profileRepository.findByName(username)
				.orElseThrow(() -> {
					log.error("User '{}' not found", username);
					return new UsernameNotFoundException(String.format("User '%s' not found", username));
				});

		final User user = Optional.ofNullable(profile.getUser())
				.orElseThrow(() -> {
					log.error("User '{}' not found in profile.", username);
					return new UsernameNotFoundException(String.format("User '%s' not found in profile.", username));
				});

		final org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
				profile.getFirstName(),
				user.getPassword(),
				mapRolesToAuthorities(user.getRoles())
		);
		log.info("Ending method loadUserByUsername: {}", userDetails);

		return userDetails;
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}
}
