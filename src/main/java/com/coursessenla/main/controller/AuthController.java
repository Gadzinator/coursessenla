package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.JwtRequest;
import com.coursessenla.main.domain.dto.JwtResponse;
import com.coursessenla.main.domain.dto.PasswordChangeRequest;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.service.AuthService;
import com.coursessenla.main.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AuthController {

	private final UserService userService;
	private final AuthService authService;

	@PostMapping("/auth")
	public ResponseEntity<?> createToken(@RequestBody JwtRequest authRequest) {
		log.info("Starting method createToken: {}", authRequest);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			log.warn("Authentication object is null. Possible unauthorized request.");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		log.info("Ending method createToken: {}", authRequest);

		return ResponseEntity.ok(new JwtResponse(authService.createAuthToken(authRequest)));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/registration")
	public UserDto createNewUser(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
		log.info("Starting method createNewUser: {}", registrationUserDto);
		final UserDto newUser = userService.createNewUser(registrationUserDto);
		log.info("Ending method createNewUser: {}", newUser);

		return newUser;
	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest, Principal principal) {
		userService.changePassword(principal.getName(), passwordChangeRequest);

		return ResponseEntity.ok("Password changed successfully");
	}
}
