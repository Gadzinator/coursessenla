package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.domain.dto.RoleDto;
import com.coursessenla.main.domain.dto.UserRoleChangeRequest;
import com.coursessenla.main.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {

	private static final String USER_FIRST_NAME_ALEX = "Alex";
	private static final String USER_LAST_NAME_PETROV = "Petrov";
	private static final String PASSWORD_ALEX = "passwordAlex";
	private static final String USER_EMAIL_ALEX = "alex@gmail.com";
	private final static String ROLE_ADMIN = "ADMIN";
	private final static String ROLE_TEST = "TEST";
	private static final long ROLE_ID = 2;
	private static final String ROLE_USER = "USER";
	private static final long NOT_FOUND_ROLE_ID = 5;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply((SecurityMockMvcConfigurers.springSecurity())).build();
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void testChangeUserRole() throws Exception {
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		userService.createNewUser(registrationUserDto);
		UserRoleChangeRequest request = createUserRoleChangeRequest();

		mockMvc.perform(put("/admins/changeUserRole")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void testChangeUserRoleWhenIsUnauthorized() throws Exception {
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		userService.createNewUser(registrationUserDto);
		UserRoleChangeRequest request = createUserRoleChangeRequest();

		mockMvc.perform(put("/admins/changeUserRole")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void testChangeUserRoleUserNotFound() throws Exception {
		UserRoleChangeRequest request = createUserRoleChangeRequest();

		mockMvc.perform(put("/admins/changeUserRole", request)
						.content(objectMapper.writeValueAsString(request))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(result -> assertEquals("There is no user with that name: " + request.getUserName(),
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void testSaveWhenHttpStatusCreated() throws Exception {
		final RoleDto roleDto = createRoleDto();
		mockMvc.perform(post("/admins/roles")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(roleDto)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "USER")
	void testSaveWhenHttpStatusForbidden() throws Exception {
		final RoleDto roleDto = createRoleDto();
		mockMvc.perform(post("/admins/roles")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(roleDto)))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/admins/roles")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void findByIdWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/admins/id/{id}", ROLE_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(ROLE_ID))
				.andExpect(jsonPath("$.name").value(ROLE_USER));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "USER")
	void findByIdWhenHttpStatusForbidden() throws Exception {
		mockMvc.perform(get("/admins/id/{id}", ROLE_ID))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void findByIdWhenRoleNotFoundException() throws Exception {
		mockMvc.perform(get("/admins/id/{id}", NOT_FOUND_ROLE_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Role with id " + NOT_FOUND_ROLE_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void findByNameWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/admins/{name}", ROLE_USER))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(ROLE_ID))
				.andExpect(jsonPath("$.name").value(ROLE_USER));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "USER")
	void findByNameWhenHttpStatusForbidden() throws Exception {
		mockMvc.perform(get("/admins/{name}", ROLE_USER))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void findByNameWhenRoleNotFoundException() throws Exception {
		mockMvc.perform(get("/admins/{name}", NOT_FOUND_ROLE_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Role with name " + NOT_FOUND_ROLE_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/admins")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(2))
				.andExpect(jsonPath("$.totalElements").value(2));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "USER")
	void findAllWhenHttpStatusForbidden() throws Exception {
		mockMvc.perform(get("/admins")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	private UserRoleChangeRequest createUserRoleChangeRequest() {
		UserRoleChangeRequest request = new UserRoleChangeRequest();
		request.setUserName(USER_FIRST_NAME_ALEX);
		request.setNewRole(ROLE_ADMIN);

		return request;
	}

	private RegistrationUserDto createRegistrationUserDto() {
		RegistrationUserDto registrationUserDto = new RegistrationUserDto();
		registrationUserDto.setFirstName(USER_FIRST_NAME_ALEX);
		registrationUserDto.setLastName(USER_LAST_NAME_PETROV);
		registrationUserDto.setPassword(PASSWORD_ALEX);
		registrationUserDto.setConfirmPassword(PASSWORD_ALEX);
		registrationUserDto.setEmail(USER_EMAIL_ALEX);

		return registrationUserDto;
	}

	private RoleDto createRoleDto() {
		RoleDto roleDto = new RoleDto();
		roleDto.setName(ROLE_TEST);

		return roleDto;
	}
}
