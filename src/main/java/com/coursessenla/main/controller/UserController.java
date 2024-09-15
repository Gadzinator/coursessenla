package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	private final JsonUtils jsonUtils;

	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		log.info("Starting method createNewUser with RegistrationUserDto: {}", registrationUserDto);

		UserDto newUser = userService.createNewUser(registrationUserDto);

		String json = jsonUtils.getJson(newUser);
		log.info("Ending method createNewUser: {}", json);

		return newUser;
	}

	public UserDto findById(long id) {
		log.info("Starting method findById with id: {}", id);

		UserDto userDto = userService.findById(id);

		String json = jsonUtils.getJson(userDto);
		log.info("Ending method findById: {}", json);

		return userDto;
	}

	public UserDto findByEmail(String email) {
		log.info("Starting method findByEmail with email: {}", email);

		final UserDto userDto = userService.findByEmail(email);

		String json = jsonUtils.getJson(userDto);
		log.info("Ending method findByEmail: {}", json);

		return userDto;
	}

	public List<UserDto> findAll() {
		log.info("Starting method findAll");

		final List<UserDto> userDtoList = userService.findAll();

		final String json = jsonUtils.getJson(userDtoList);
		log.info("Ending method findAll: {}", json);

		return userDtoList;
	}

	public void deleteById(long id) {
		log.info("Starting method deleteById: with id: {}", id);

		userService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", String.format("User with Id %d has been successfully deleted", id));
		String json = jsonUtils.getJson(response);
		log.info("Ending method deleteById. Deletion response: {}", json);
	}
}
