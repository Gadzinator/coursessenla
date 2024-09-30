package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.exception.UserNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.PlayListRepositoryImpl;
import com.coursessenla.main.repository.impl.ProfileRepositoryImpl;
import com.coursessenla.main.repository.impl.UserRepositoryImpl;
import com.coursessenla.main.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepositoryImpl userRepository;
	private final ProfileRepositoryImpl profileRepository;
	private final PlayListRepositoryImpl playListRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public UserDto createNewUser(RegistrationUserDto registrationUserDto) {
		log.info("Starting method createNewUser: {}", registrationUserDto);

		final User user = mapper.mapToEntity(registrationUserDto, User.class);
		user.setPassword(registrationUserDto.getPassword());

		final Profile profile = createProfile(registrationUserDto);
		profile.setUser(user);

		userRepository.save(user);
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
		log.info("Ending method deleteById: {}", id);
	}

	private Profile createProfile(RegistrationUserDto registrationUserDto) {
		Profile profile = new Profile();
		profile.setFirstName(registrationUserDto.getFirstName());
		profile.setLastName(registrationUserDto.getLastName());
		return profile;
	}
}
