package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.JwtRequest;
import com.coursessenla.main.domain.dto.PasswordChangeRequest;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.coursessenla.main.service.impl.AuthServiceImpl;
import com.coursessenla.main.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

	private static final String USER_FIRST_NAME_ALEX = "Alex";
	private static final String USER_LAST_NAME_PETROV = "Petrov";
	private static final String USER_NAME_OLEG = "Oleg";
	private static final String PASSWORD_ALEX = "passwordAlex";
	private static final String PASSWORD_OLEG = "passwordOleg";
	private static final String NEW_PASSWORD = "newPassword";
	private static final String USER_EMAIL_ALEX = "alex@gmail.com";
	private static final String USER_EMAIL_OLEG = "oleg@gmail.com";
	private static final String USER_ROLE = "USER_ROLE";

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private AuthServiceImpl authService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		init();
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply((SecurityMockMvcConfigurers.springSecurity())).build();
	}

	@Test
	@WithUserDetails(value = USER_FIRST_NAME_ALEX, setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authServiceImpl")
	void testCreateAuthTokenWhenTokenHttpStatusNotFound() throws Exception {
		JwtRequest authRequest = new JwtRequest();
		authRequest.setUsername("username");
		authRequest.setPassword("password");

		SecurityContextHolder.clearContext();
		mockMvc.perform(post("/auth")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithAnonymousUser
	void testCreateAuthTokenWhenTokenHttpStatusIsNotFoundUser() throws Exception {
		JwtRequest authRequest = new JwtRequest();
		authRequest.setUsername("username");
		authRequest.setPassword("password");

		SecurityContextHolder.clearContext();
		mockMvc.perform(post("/auth")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isNotFound());
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authServiceImpl")
	void testCreateAuthTokenWhenTokenHttpStatusIsOk() throws Exception {
		JwtRequest authRequest = new JwtRequest();
		authRequest.setUsername(USER_FIRST_NAME_ALEX);
		authRequest.setPassword(PASSWORD_ALEX);

		authService.createAuthToken(authRequest);
		mockMvc.perform(post("/auth")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(authRequest)))
				.andExpect(status().isOk());
	}

	@Test
	void testCreateNewUserWhenHttpStatusCreated() throws Exception {
		RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		registrationUserDto.setFirstName(USER_NAME_OLEG);
		registrationUserDto.setLastName(USER_LAST_NAME_PETROV);
		registrationUserDto.setPassword(PASSWORD_OLEG);
		registrationUserDto.setConfirmPassword(PASSWORD_OLEG);
		registrationUserDto.setEmail(USER_EMAIL_OLEG);

		mockMvc.perform(post("/registration")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registrationUserDto)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authServiceImpl")
	void testCreateNewUserWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/registration")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authServiceImpl")
	void testChangePasswordHttpStatusOk() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setOldPassword(PASSWORD_ALEX);
		passwordChangeRequest.setNewPassword(NEW_PASSWORD);

		mockMvc.perform(post("/changePassword")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordChangeRequest))
						.principal(new TestingAuthenticationToken(USER_FIRST_NAME_ALEX, PASSWORD_ALEX, USER_ROLE)))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails(value = "Alex", setupBefore = TestExecutionEvent.TEST_EXECUTION, userDetailsServiceBeanName = "authServiceImpl")
	void testChangePasswordHttpStatusBadRequest() throws Exception {
		PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
		passwordChangeRequest.setOldPassword("");
		passwordChangeRequest.setNewPassword(NEW_PASSWORD);

		mockMvc.perform(post("/changePassword")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(passwordChangeRequest))
						.principal(new TestingAuthenticationToken(USER_FIRST_NAME_ALEX, PASSWORD_ALEX, USER_ROLE)))
				.andExpect(status().isBadRequest());
	}

	private void init() {
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		userService.createNewUser(registrationUserDto);
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
}