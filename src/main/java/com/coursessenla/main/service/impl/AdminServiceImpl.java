package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.RoleDto;
import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.domain.entity.Role;
import com.coursessenla.main.domain.entity.User;
import com.coursessenla.main.exception.RoleNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.ProfileRepositoryImpl;
import com.coursessenla.main.repository.impl.RoleRepositoryImpl;
import com.coursessenla.main.repository.impl.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements com.coursessenla.main.service.AdminService {

	private final ProfileRepositoryImpl profileRepository;
	private final RoleRepositoryImpl roleRepository;
	private final UserRepositoryImpl userRepository;
	private final GenericMapper mapper;

	@Override
	@Transactional
	public void changeUserRole(String username, String newRole) {
		log.info("Starting method changeUserRole with username: {} and newRole: {}", username, newRole);
		final User user = findUserByUsername(username);

		RoleDto roleDto;
		try {
			roleDto = findRoleByName(newRole);
		} catch (RoleNotFoundException e) {
			log.info("Role not found, creating new role: {}", newRole);
			roleDto = new RoleDto();
			roleDto.setName(newRole);
			saveRole(roleDto);
		}

		final Role role = mapper.mapToEntity(roleDto, Role.class);
		if (!user.getRoles().contains(role)) {
			user.getRoles().add(role);
		}

		userRepository.save(user);
		log.info("Ending method changeUserRole: {}", user);
	}

	@Transactional
	@Override
	public void saveRole(RoleDto roleDto) {
		log.info("Starting method save: {}", roleDto);
		final Role role = mapper.mapToEntity(roleDto, Role.class);
		roleRepository.save(role);
		log.info("Ending method save: {}", roleDto);
	}

	@Transactional
	@Override
	public RoleDto findRoleById(long id) {
		log.info("Starting method findById: {}", id);
		final RoleDto roleDto = roleRepository.findById(id)
				.map(role -> mapper.mapToDto(role, RoleDto.class))
				.orElseThrow(() -> new RoleNotFoundException(id));
		log.info("Ending method findById: {}", roleDto);

		return roleDto;
	}

	@Transactional
	@Override
	public RoleDto findRoleByName(String name) {
		log.info("Starting method findByName: {}", name);
		final RoleDto roleDto = roleRepository.findByName(name)
				.map(role -> mapper.mapToDto(role, RoleDto.class))
				.orElseThrow(() -> new RoleNotFoundException(name));
		log.info("Ending method findByName: {}", roleDto);

		return roleDto;
	}

	@Transactional
	@Override
	public Page<RoleDto> findAllRoles(Pageable pageable) {
		log.info("Starting method findAll: {}", pageable);
		final Page<RoleDto> roleDtoPage = roleRepository.findAll(pageable)
				.map(role -> mapper.mapToDto(role, RoleDto.class));

		if (roleDtoPage.isEmpty()) {
			log.warn("No roles were found, throwing RoleNotFoundException");
			throw new RoleNotFoundException();
		}

		log.info("Ending method findAll. Total elements: {}", roleDtoPage.getTotalElements());

		return roleDtoPage;
	}

	private User findUserByUsername(String username) {
		log.info("Starting method findUserByUsername with username: {}", username);
		final User user = profileRepository.findByName(username)
				.map(Profile::getUser)
				.orElseThrow(() -> {
					log.warn("User not found with username: {}", username);
					return new UsernameNotFoundException("There is no user with that name: " + username);
				});
		log.info("Ending method findUserByUsername: found user with username: {}", username);

		return user;
	}
}
