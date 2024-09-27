package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.RegistrationUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

	private static final long USER_ID = 1;
	private static final long NOT_FOUND_USER_ID = 52;
	private final static String USER_EMAIL = "client1@example.com";
	private static final String USER_PASSWORD = "password1";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void testCreateNewUserWhenHttpStatusCreated() throws Exception {
		final RegistrationUserDto registrationUserDto = createRegistrationUserDto();
		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(registrationUserDto)))
				.andExpect(status().isCreated());
	}

	@Test
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByIdWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/users/id/{id}", USER_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(USER_ID));
	}

	@Test
	void findByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/users/id/{id}", NOT_FOUND_USER_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("User with id " + NOT_FOUND_USER_ID + " was not found",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	void findByEmailWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/users/{email}", USER_EMAIL))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(USER_ID))
				.andExpect(jsonPath("$.email").value(USER_EMAIL));
	}

	@Test
	void findByEMailWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/users/{email}", NOT_FOUND_USER_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("User with email " + NOT_FOUND_USER_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/users")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(50))
				.andExpect(jsonPath("$.totalElements").value(50));
	}

	@Test
	void deleteByIdWhenHttpStatusNoContent() throws Exception {
		mockMvc.perform(delete("/users/{id}", USER_ID))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(delete("/users/{id}", NOT_FOUND_USER_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("User with id " + NOT_FOUND_USER_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	private RegistrationUserDto createRegistrationUserDto() {
		RegistrationUserDto registrationUserDto = new RegistrationUserDto();
		registrationUserDto.setFirstName("Pushkin");
		registrationUserDto.setLastName("Aleksandr");
		registrationUserDto.setPassword(USER_PASSWORD);
		registrationUserDto.setConfirmPassword(USER_PASSWORD);
		registrationUserDto.setEmail(USER_EMAIL);

		return registrationUserDto;
	}
}