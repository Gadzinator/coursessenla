package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.RoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
	void changeUserRole(String username, String newRole);

	void saveRole(RoleDto roleDto);

	RoleDto findRoleById(long id);

	RoleDto findRoleByName(String name);

	Page<RoleDto> findAllRoles(Pageable pageable);
}
