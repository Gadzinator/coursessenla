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
import com.coursessenla.main.service.impl.config.ServiceTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
class AdminServiceImplTest {

	private static final String ROLE_USER = "ROLE_USER";
	private static final String ROLE_TEST = "ROLE_TEST";
	private static final String USER_FIRST_NAME_ALEX = "Alex";
	private static final long ROLE_ID = 1;
	private static final long USER_ID = 1;
	private static final long PROFILE_ID = 1;

	@Mock
	private ProfileRepositoryImpl profileRepository;

	@Mock
	private RoleRepositoryImpl roleRepository;

	@Mock
	private UserRepositoryImpl userRepository;

	@Mock
	private GenericMapper mapper;

	@InjectMocks
	private AdminServiceImpl adminService;

	@Test
	void testChangeUserRoleWhenAddNewRole() {
		final Role existingRole = createRole();
		final User user = createUser(existingRole);
		final Profile profile = createProfile(user);

		when(profileRepository.findByName(USER_FIRST_NAME_ALEX)).thenReturn(Optional.of(profile));
		when(roleRepository.findByName(ROLE_TEST)).thenReturn(Optional.empty());
		doNothing().when(roleRepository).save(any(Role.class));
		when(mapper.mapToEntity(any(RoleDto.class), eq(Role.class))).thenAnswer(invocation -> {
			RoleDto dto = invocation.getArgument(0);
			Role role = new Role();
			role.setId(2L);
			role.setName(dto.getName());
			return role;
		});

		adminService.changeUserRole(USER_FIRST_NAME_ALEX, ROLE_TEST);

		verify(profileRepository).findByName(USER_FIRST_NAME_ALEX);
		verify(roleRepository).findByName(ROLE_TEST);
		verify(mapper, times(2)).mapToEntity(any(RoleDto.class), eq(Role.class));
		verify(roleRepository).save(any(Role.class));
		assertEquals(2, user.getRoles().size());
		assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_TEST)));
	}

	@Test
	void testChangeUserRoleWhenUserNotFound() {
		when(profileRepository.findByName(USER_FIRST_NAME_ALEX)).thenReturn(Optional.empty());

		assertThrows(UsernameNotFoundException.class, () ->
				adminService.changeUserRole(USER_FIRST_NAME_ALEX, ROLE_USER));

		verify(profileRepository).findByName(USER_FIRST_NAME_ALEX);
		verifyNoMoreInteractions(profileRepository);
	}

	@Test
	void testChangeUserRoleWhenRoleFound() {
		final Role role = createRole();
		final Role existingRole = createRole();
		existingRole.setName(ROLE_TEST);
		final User user = createUser(role);
		final Profile profile = createProfile(user);

		when(profileRepository.findByName(USER_FIRST_NAME_ALEX)).thenReturn(Optional.of(profile));
		when(roleRepository.findByName(ROLE_TEST)).thenReturn(Optional.of(existingRole));

		adminService.changeUserRole(USER_FIRST_NAME_ALEX, ROLE_TEST);

		verify(profileRepository).findByName(USER_FIRST_NAME_ALEX);
		verify(roleRepository).findByName(ROLE_TEST);
		verify(roleRepository, never()).save(any(Role.class));
		verify(userRepository).save(user);
		assertEquals(2, user.getRoles().size());
		assertTrue(user.getRoles().stream().anyMatch(r -> ROLE_USER.equals(r.getName())));
	}

	@Test
	void testSave() {
		final Role role = createRole();
		final RoleDto roleDto = createRoleDto();

		when(mapper.mapToEntity(roleDto, Role.class)).thenReturn(role);
		doNothing().when(roleRepository).save(any(Role.class));

		adminService.saveRole(roleDto);

		verify(mapper).mapToEntity(roleDto, Role.class);
		verify(roleRepository).save(role);
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final Role role = createRole();
		final RoleDto roleDto = createRoleDto();

		when(mapper.mapToEntity(roleDto, Role.class)).thenReturn(role);
		doThrow(new RuntimeException("Database error")).when(roleRepository).save(role);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> adminService.saveRole(roleDto));

		assertEquals("Database error", exception.getMessage());
		verify(mapper).mapToEntity(roleDto, Role.class);
		verify(roleRepository).save(role);
	}

	@Test
	void testFindByIdWhenRoleExist() {
		final Role role = createRole();
		final RoleDto roleDto = createRoleDto();

		when(mapper.mapToDto(role, RoleDto.class)).thenReturn(roleDto);
		when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.of(role));

		final RoleDto existRoleDto = adminService.findRoleById(ROLE_ID);

		verify(mapper).mapToDto(role, RoleDto.class);
		verify(roleRepository).findById(ROLE_ID);
		assertEquals(roleDto, existRoleDto);
	}

	@Test
	void testFindByIdWhenRoleNotExist() {
		when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.empty());

		assertThrows(RoleNotFoundException.class, () -> adminService.findRoleById(ROLE_ID));
	}

	@Test
	void findByNameRoleExist() {
		final Role role = createRole();
		final RoleDto roleDto = createRoleDto();

		when(mapper.mapToDto(role, RoleDto.class)).thenReturn(roleDto);
		when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.of(role));

		final RoleDto existRoleDto = adminService.findRoleByName(ROLE_USER);

		verify(mapper).mapToDto(role, RoleDto.class);
		verify(roleRepository).findByName(ROLE_USER);
		assertEquals(roleDto, existRoleDto);
	}

	@Test
	void findByNameRoleNotExist() {
		when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.empty());

		assertThrows(RoleNotFoundException.class, () -> adminService.findRoleByName(ROLE_USER));
	}

	@Test
	void findAll() {
		final Role role = createRole();
		final RoleDto roleDto = createRoleDto();
		final Pageable pageable = PageRequest.of(0, 10);
		Page<Role> rolePage = new PageImpl<>(Collections.singletonList(role), pageable, 1);

		when(roleRepository.findAll(pageable)).thenReturn(rolePage);
		when(mapper.mapToDto(role, RoleDto.class)).thenReturn(roleDto);

		Page<RoleDto> result = adminService.findAllRoles(pageable);

		verify(roleRepository).findAll(pageable);
		verify(mapper).mapToDto(role, RoleDto.class);

		assertFalse(result.isEmpty());
		assertEquals(1, result.getTotalElements());
		assertEquals(roleDto, result.getContent().get(0));
	}

	@Test
	void testFindAllWhenListRolesEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(roleRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(RoleNotFoundException.class, () -> adminService.findAllRoles(pageable));
	}

	private Profile createProfile(User user) {
		Profile profile = new Profile();
		profile.setId(PROFILE_ID);
		profile.setFirstName(USER_FIRST_NAME_ALEX);
		profile.setUser(user);

		return profile;
	}

	private User createUser(Role role) {
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		User user = new User();
		user.setId(USER_ID);
		user.setRoles(roles);

		return user;
	}

	private Role createRole() {
		Role role = new Role();
		role.setId(ROLE_ID);
		role.setName(ROLE_USER);

		return role;
	}

	private RoleDto createRoleDto() {
		RoleDto roleDto = new RoleDto();
		roleDto.setId(ROLE_ID);
		roleDto.setName(ROLE_USER);

		return roleDto;
	}
}
