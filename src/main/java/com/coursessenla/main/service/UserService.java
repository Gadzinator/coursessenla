package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;

import java.util.List;

public interface UserService {
	UserDto createNewUser(RegistrationUserDto registrationUserDto);

	UserDto findById(long id);

	UserDto findByEmail(String email);

	List<UserDto> findAll();

	void deleteById(long id);
}
