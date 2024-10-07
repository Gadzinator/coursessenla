package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.PasswordChangeRequest;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
	UserDto createNewUser(RegistrationUserDto registrationUserDto);

	UserDto findById(long id);

	UserDto findByEmail(String email);

	Page<UserDto> findAll(Pageable pageable);

	void deleteById(long id);

	void changePassword(String userName, PasswordChangeRequest passwordChangeRequest);
}
