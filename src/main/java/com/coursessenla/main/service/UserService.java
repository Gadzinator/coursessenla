package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;

public interface UserService {
	UserDto createNewUser(RegistrationUserDto registrationUserDto);

	UserDto findById(long id);

	void deleteById(long id);
}
