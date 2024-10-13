package com.coursessenla.main.controller;

import com.coursessenla.main.domain.dto.RoleDto;
import com.coursessenla.main.domain.dto.UserRoleChangeRequest;
import com.coursessenla.main.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admins")
public class AdminController {

	private final AdminService adminService;

	@PutMapping("/changeUserRole")
	public String changeUserRole(@RequestBody UserRoleChangeRequest userRoleChangeRequest) {
		log.info("Starting method changeUserRole: {}", userRoleChangeRequest);
		adminService.changeUserRole(userRoleChangeRequest.getUserName(), userRoleChangeRequest.getNewRole());
		log.info("Ending method changeUserRole: {}", userRoleChangeRequest);

		return "Role change";
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/roles")
	public void saveRole(@RequestBody @Valid RoleDto roleDto) {
		log.info("Starting method save: {}", roleDto);
		adminService.saveRole(roleDto);
		log.info("Ending method save: {}", roleDto);
	}

	@GetMapping("/id/{id}")
	public RoleDto findRoleById(@PathVariable(value = "id") Long id) {
		log.info("Starting method findById with id: {}", id);
		final RoleDto roleDto = adminService.findRoleById(id);
		log.info("Ending method findById {}", roleDto);

		return roleDto;
	}

	@GetMapping("/{name}")
	public RoleDto findRoleByName(@PathVariable(value = "name") String name) {
		log.info("Starting method findByName with name: {}", name);
		final RoleDto roleDto = adminService.findRoleByName(name);
		log.info("Ending method findByName: {}", roleDto);

		return roleDto;
	}

	@GetMapping()
	public Page<RoleDto> findAllRoles(@RequestParam(value = "page", defaultValue = "0") int page,
									  @RequestParam(value = "size", defaultValue = "50") int size) {
		log.info("Starting method findAll with page: {} and size: {}", page, size);
		Pageable pageable = PageRequest.of(page, size);
		final Page<RoleDto> roleDtoPage = adminService.findAllRoles(pageable);
		log.info("Ending method findAlle: {}", roleDtoPage.getTotalElements());

		return roleDtoPage;
	}
}
