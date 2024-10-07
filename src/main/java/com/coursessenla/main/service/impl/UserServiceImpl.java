package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.PasswordChangeRequest;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.Role;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.exception.EmailNotUniqueException;
import com.coursessenla.main.exception.PasswordMismatchException;
import com.coursessenla.main.exception.UserNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.PlayListRepositoryImpl;
import com.coursessenla.main.repository.impl.ProfileRepositoryImpl;
import com.coursessenla.main.repository.impl.RoleRepositoryImpl;
import com.coursessenla.main.repository.impl.UserRepositoryImpl;
import com.coursessenla.main.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepositoryImpl userRepository;
	private final ProfileRepositoryImpl profileRepository;
	private final PlayListRepositoryImpl playListRepository;
	private final RoleRepositoryImpl roleRepository;
	private final GenericMapper mapper;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		log.info("Starting method createNewUser: {}", registrationUserDto);
		validateEmailIsUnique(registrationUserDto.getEmail());
		validatePasswordMatch(registrationUserDto.getPassword(), registrationUserDto.getConfirmPassword());

		final User user = mapper.mapToEntity(registrationUserDto, User.class);
		final String encode = passwordEncoder.encode(registrationUserDto.getPassword());
		user.setPassword(encode);

		Role role = roleRepository.findByName("ROLE_USER")
				.orElseGet(() -> {
					Role newRoleEntity = new Role("ROLE_USER");
					roleRepository.save(newRoleEntity);
					log.info("Saved role in method createUser: {}", newRoleEntity);
					return newRoleEntity;
				});

		List<Role> roles = Collections.singletonList(role);
		user.setRoles(roles);

		userRepository.save(user);
		final Profile profile = createProfile(registrationUserDto);
		profile.setUser(user);

		profileRepository.save(profile);
		log.info("Ending method createNewUser: {}", registrationUserDto);

		return mapper.mapToDto(user, UserDto.class);
	}

	@Override
	public UserDto findById(long id) {
		log.info("Starting method findById: {}", id);
		final UserDto userDto = userRepository.findById(id)
				.map(user -> mapper.mapToDto(user, UserDto.class))
				.orElseThrow(() -> new UserNotFoundException(id));
		log.info("Ending method findById: {}", userDto);

		return userDto;
	}

	@Override
	public UserDto findByEmail(String email) {
		log.info("Starting method findByEmail: {}", email);
		final UserDto userDto = userRepository.findByEmail(email)
				.map(user -> mapper.mapToDto(user, UserDto.class))
				.orElseThrow(() -> new UserNotFoundException(email));
		log.info("Ending method findByEmail: {}", userDto);

		return userDto;
	}

	@Override
	public Page<UserDto> findAll(Pageable pageable) {
		log.info("Starting method findAll: {}", pageable);
		final Page<UserDto> userDtoPage = userRepository.findAll(pageable)
				.map(user -> mapper.mapToDto(user, UserDto.class));

		if (userDtoPage.isEmpty()) {
			log.warn("No users were found, throwing ReviewNotFoundException");
			throw new UserNotFoundException();
		}

		log.info("Ending method findAll: {}", userDtoPage);

		return userDtoPage;
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting method deleteById: {}", id);
		final User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(id));
		if (user.getPlaylists() != null) {
			user.getPlaylists().forEach(playlist -> {
				playlist.getMovies().forEach(movie -> movie.getPlaylists().remove(playlist));
				playListRepository.deleteById(playlist.getId());
			});
		}

		userRepository.deleteById(id);
	}

	@Override
	@Transactional
	public void changePassword(String username, PasswordChangeRequest passwordChangeRequest) {
		log.info("Starting change password for user: " + username);

		String enteredPassword = passwordChangeRequest.getOldPassword();
		final Optional<Profile> optionalProfile = profileRepository.findByName(username);
		if (optionalProfile.isEmpty()) {
			throw new UserNotFoundException();
		}
		final Profile profile = optionalProfile.get();
		final User user = profile.getUser();

		String storedPassword = user.getPassword();

		if (!passwordEncoder.matches(enteredPassword, storedPassword)) {
			log.error("Password change failed: Passwords do not match for user: " + username);
			throw new PasswordMismatchException();
		}

		user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
		userRepository.save(user);

		log.info("Finished password changed successfully for user: " + username);
	}

	private Profile createProfile(RegistrationUserDto registrationUserDto) {
		Profile profile = new Profile();
		profile.setFirstName(registrationUserDto.getFirstName());
		profile.setLastName(registrationUserDto.getLastName());
		return profile;
	}

	private void validateEmailIsUnique(String email) {
		log.info("Starting validating email uniqueness");
		final Optional<User> optionalUser = userRepository.findByEmail(email);
		if (optionalUser.isPresent()) {
			log.error("Email" + email + "is already in use.");
			throw new EmailNotUniqueException(email);
		}

		log.info("Ending validating email uniqueness");
	}

	private void validatePasswordMatch(String password, String confirmPassword) {
		log.info("Starting validating password match: " + password + ", and " + confirmPassword);
		if (!password.equals(confirmPassword)) {
			log.error("Passwords do not match");
			throw new PasswordMismatchException();
		}
		log.info("Finished validating password math");
	}

}
