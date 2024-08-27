package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.ProfileRepository;
import com.coursessenla.main.repository.UserRepository;
import com.coursessenla.main.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ProfileRepository profileRepository;
//	private final UserMapper userMapper;
	private final GenericMapperImpl<User, UserDto> userMapper;
	private final GenericMapperImpl<User, RegistrationUserDto> registrationUserDtoMapper;

	@Override
	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
//		final User user = userMapper.registrationUserDtoToEntity(registrationUserDto);
		final User user = registrationUserDtoMapper.toEntity(registrationUserDto);
		user.setPassword(registrationUserDto.getPassword());

		final Profile profile = createProfile(registrationUserDto);
		profile.setUser(user);

		profileRepository.save(profile);
		userRepository.save(user);
		return userMapper.toDto(user);
	}

	@Override
	public UserDto findById(long id) {
		return userRepository.findById(id)
				.map(userMapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("User with id %d was not found", id)));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		userRepository.deleteById(id);
	}

	private Profile createProfile(RegistrationUserDto registrationUserDto) {
		Profile profile = new Profile();
		profile.setFirstName(registrationUserDto.getFirstName());
		profile.setLastName(registrationUserDto.getLastName());
		return profile;
	}
}
