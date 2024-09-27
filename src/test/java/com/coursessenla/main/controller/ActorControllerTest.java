package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.ActorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
class ActorControllerTest {

	private static final long ACTOR_ID = 1;
	private static final String ACTOR_NAME = "Actor 1";
	private static final long NOT_FOUND_ACTOR_ID = 53;
	private static final String NOT_FOUND_ACTOR_NAME = "Actor 100";

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
	void testSaveWhenHttpStatusCreated() throws Exception {
		final ActorDto actorDto = createActorDto();
		mockMvc.perform(post("/actors")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(actorDto)))
				.andExpect(status().isCreated());
	}

	@Test
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/actors")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByIdWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/actors/id/{id}", ACTOR_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(ACTOR_ID))
				.andExpect(jsonPath("$.name").value(ACTOR_NAME));
	}

	@Test
	void findByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/actors/id/{id}", NOT_FOUND_ACTOR_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Actor with id " + NOT_FOUND_ACTOR_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void findByNameWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/actors/{name}", ACTOR_NAME))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(ACTOR_ID))
				.andExpect(jsonPath("$.name").value(ACTOR_NAME));
	}

	@Test
	void findByNameWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/actors/{name}", NOT_FOUND_ACTOR_NAME))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Actor with name " + NOT_FOUND_ACTOR_NAME + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/actors")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(50))
				.andExpect(jsonPath("$.totalElements").value(50));
	}

	@Test
	void updateWhenHttpStatusOk() throws Exception {
		final ActorDto actorDto = createActorDto();
		actorDto.setId(ACTOR_ID);
		mockMvc.perform(put("/actors", actorDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(actorDto)))
				.andExpect(status().isOk());
	}

	@Test
	void updateWhenActorNotFoundException() throws Exception {
		final ActorDto actorDto = createActorDto();
		actorDto.setId(NOT_FOUND_ACTOR_ID);
		mockMvc.perform(put("/actors")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(actorDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Actor with id " + NOT_FOUND_ACTOR_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void deleteByIdWhenHttpStatusNoContent() throws Exception {
		mockMvc.perform(delete("/actors/{id}", ACTOR_ID))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(delete("/actors/{id}", NOT_FOUND_ACTOR_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Actor with id " + NOT_FOUND_ACTOR_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	private ActorDto createActorDto() {
		ActorDto actorDto = new ActorDto();
		actorDto.setName(ACTOR_NAME);
		actorDto.setBirthDate("1984-11-11");
		actorDto.setNationality("American");
		actorDto.setAwards("Oscar");

		return actorDto;
	}
}
