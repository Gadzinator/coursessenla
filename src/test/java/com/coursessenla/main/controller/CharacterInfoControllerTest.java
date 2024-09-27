package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.CharacterInfoId;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateConfig.class, LiquibaseConfig.class, WebMvcConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CharacterInfoControllerTest {

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
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		mockMvc.perform(post("/characterInfos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(characterInfoDto)))
				.andExpect(status().isCreated());
	}

	@Test
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/characterInfos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByIdWhenHttpStatusOk() throws Exception {
		final long movieId = 1L;
		final long actorId = 1L;
		mockMvc.perform(get("/characterInfos/id")
						.param("movieId", Long.toString(movieId))
						.param("actorId", Long.toString(actorId)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	@Test
	void findByIdWhenActorNotFoundException() throws Exception {
		final long movieId = 51L;
		final long actorId = 51L;
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		characterInfoId.setMovieId(movieId);
		characterInfoId.setActorId(actorId);
		mockMvc.perform(get("/characterInfos/id")
						.param("movieId", Long.toString(movieId))
						.param("actorId", Long.toString(actorId)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("CharacterInfo with id " + characterInfoId + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/characterInfos")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(50))
				.andExpect(jsonPath("$.totalElements").value(50));
	}

	@Test
	void updateWhenHttpStatusOk() throws Exception {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		CharacterInfoDto updateCharacterInfoDto = createCharacterInfoDto(characterInfoId);
		mockMvc.perform(put("/characterInfos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateCharacterInfoDto)))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id.movieId").value(characterInfoId.getMovieId()))
				.andExpect(jsonPath("$.id.actorId").value(characterInfoId.getActorId()));
	}

	@Test
	void updateWhenActorNotFoundException() throws Exception {
		final CharacterInfoId characterInfoId = new CharacterInfoId();
		characterInfoId.setMovieId(51L);
		characterInfoId.setActorId(51L);
		CharacterInfoDto updateCharacterInfoDto = createCharacterInfoDto(characterInfoId);
		mockMvc.perform(put("/characterInfos")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateCharacterInfoDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("CharacterInfo with id " + characterInfoId + " was not found",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	void deleteByIdWhenHttpStatusNoContent() throws Exception {
		final long movieId = 1L;
		final long actorId = 1L;
		mockMvc.perform(delete("/characterInfos/id")
						.param("movieId", Long.toString(movieId))
						.param("actorId", Long.toString(actorId)))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteByIdWhenActorNotFoundException() throws Exception {
		final long movieId = 51L;
		final long actorId = 51L;
		mockMvc.perform(delete("/characterInfos/id")
						.param("movieId", Long.toString(movieId))
						.param("actorId", Long.toString(actorId)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("CharacterInfo with id CharacterInfoId{movieId=51, actorId=51} was not found",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	private CharacterInfoDto createCharacterInfoDto(CharacterInfoId characterInfoId) {
		CharacterInfoDto characterInfoDto = new CharacterInfoDto();
		characterInfoDto.setId(characterInfoId);
		characterInfoDto.setMovieId(1L);
		characterInfoDto.setActorId(1L);
		characterInfoDto.setCharacterName("Dom Cobb");

		return characterInfoDto;
	}

	private CharacterInfoId createCharacterInfoId() {
		CharacterInfoId characterInfoId = new CharacterInfoId();
		characterInfoId.setActorId(1L);
		characterInfoId.setMovieId(1L);

		return characterInfoId;
	}
}
