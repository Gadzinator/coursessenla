package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public UserDto createNewUser(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
		log.info("Starting method createNewUser with RegistrationUserDto: {}", registrationUserDto);
		UserDto newUser = userService.createNewUser(registrationUserDto);
		log.info("Ending method createNewUser: {}", newUser);

		return newUser;
	}

	@GetMapping("/id/{id}")
	public UserDto findById(@PathVariable("id") Long id) {
		log.info("Starting method findById with id: {}", id);
		UserDto userDto = userService.findById(id);
		log.info("Ending method findById: {}", userDto);

		return userDto;
	}

	@GetMapping("/{email}")
	public UserDto findByEmail(@PathVariable("email") String email) {
		log.info("Starting method findByEmail with email: {}", email);
		final UserDto userDto = userService.findByEmail(email);
		log.info("Ending method findByEmail: {}", userDto);

		return userDto;
	}

	@GetMapping
	public Page<UserDto> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
								 @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<UserDto> userDtoPage = userService.findAll(pageable);
		log.info("Ending method findAll: {}", userDtoPage);

		return userDtoPage;
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable("id") Long id) {
		log.info("Starting method deleteById: with id: {}", id);
		userService.deleteById(id);
		log.info("Ending method deleteById.");
	}
}
