package com.coursessenla.main.mapper;

import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.domain.entity.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserMapper {

	private final ModelMapper mapper;

	public UserDto toDto(User user) {
		return mapper.map(user, UserDto.class);
	}

	public User toEntity(UserDto userDto) {
		return mapper.map(userDto, User.class);
	}

	public User registrationUserDtoToEntity(RegistrationUserDto registrationUserDto) {
		return mapper.map(registrationUserDto, User.class);
	}
}
