package com.coursessenla.main.controller;

import com.coursessenla.main.controller.utils.JsonUtils;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	private final JsonUtils jsonUtils;

	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		UserDto newUser = userService.createNewUser(registrationUserDto);

		String json = jsonUtils.getJson(newUser);
		log.info("Method to createNewUser to UserController - " + json);

		return newUser;
	}

	public UserDto findById(long id) {
		UserDto userDto = userService.findById(id);

		String json = jsonUtils.getJson(userDto);
		log.info("Method to findById to UserController - " + json);

		return userDto;
	}

	public void deleteById(long id) {
		userService.deleteById(id);

		Map<String, Object> response = new HashMap<>();
		response.put("Message", "User with Id " + id + " has been successfully deleted");
		String json = jsonUtils.getJson(response);
		log.info(json);
	}
}
