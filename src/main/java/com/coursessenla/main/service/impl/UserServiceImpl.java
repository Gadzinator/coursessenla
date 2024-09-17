package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.ProfileRepositoryImpl;
import com.coursessenla.main.repository.impl.UserRepositoryImpl;
import com.coursessenla.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepositoryImpl userRepository;
	private final ProfileRepositoryImpl profileRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		final User user = mapper.mapToEntity(registrationUserDto, User.class);
		user.setPassword(registrationUserDto.getPassword());

		final Profile profile = createProfile(registrationUserDto);
		profile.setUser(user);

		profileRepository.save(profile);
		userRepository.save(user);

		return mapper.mapToDto(user, UserDto.class);
	}

	@Override
	public UserDto findById(long id) {
		return userRepository.findById(id)
				.map(user -> mapper.mapToEntity(user, UserDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("User with id %d was not found", id)));
	}

	@Override
	public UserDto findByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(user -> mapper.mapToEntity(user, UserDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("User with email %s was not found", email)));
	}

	@Override
	public List<UserDto> findAll() {
		return userRepository.findAll().stream()
				.map(user -> mapper.mapToDto(user, UserDto.class))
				.collect(Collectors.toList());
	}

	@Transactional
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
