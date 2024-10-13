package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.service.GenreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
class GenreControllerTest {

	private static final long GENRE_ID = 1;
	private static final String GENRE_NAME = "Action";
	private static final long NOT_FOUND_GENRE_ID = 51;
	private static final String NOT_FOUND_GENRE_NAME = "Actions";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private GenreService genreService;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply((SecurityMockMvcConfigurers.springSecurity())).build();
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void testSaveWhenHttpStatusCreated() throws Exception {
		final GenreDto genreDto = createGenreDto();
		mockMvc.perform(post("/genres")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(genreDto)))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "USER")
	void testSaveWhenHttpStatusForbidden() throws Exception {
		final GenreDto genreDto = createGenreDto();
		mockMvc.perform(post("/genres")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(genreDto)))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/genres")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByIdWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/genres/id/{id}", GENRE_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(GENRE_ID))
				.andExpect(jsonPath("$.name").value(GENRE_NAME));
	}

	@Test
	void findByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/genres/id/{id}", NOT_FOUND_GENRE_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Genre with id " + NOT_FOUND_GENRE_ID + " was not found",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	void findByNameWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/genres/{name}", GENRE_NAME))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(GENRE_ID))
				.andExpect(jsonPath("$.name").value(GENRE_NAME));
	}

	@Test
	void findByNameWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/genres/{name}", NOT_FOUND_GENRE_NAME))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Genre with name " + NOT_FOUND_GENRE_NAME + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/genres")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(50))
				.andExpect(jsonPath("$.totalElements").value(50));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void updateWhenHttpStatusOk() throws Exception {
		final GenreDto genreDto = createGenreDto();
		genreDto.setId(GENRE_ID);
		mockMvc.perform(put("/genres", genreDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(genreDto)))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "USER")
	void updateWhenHttpStatusForbidden() throws Exception {
		final GenreDto genreDto = createGenreDto();
		genreDto.setId(GENRE_ID);
		mockMvc.perform(put("/genres", genreDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(genreDto)))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void updateWhenActorNotFoundException() throws Exception {
		final GenreDto genreDto = createGenreDto();
		genreDto.setId(NOT_FOUND_GENRE_ID);
		mockMvc.perform(put("/genres")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(genreDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Genre with id " + NOT_FOUND_GENRE_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void deleteByIdWhenHttpStatusNoContent() throws Exception {
		final GenreDto genreDto = createGenreDto();
		genreService.save(genreDto);
		mockMvc.perform(delete("/genres/{id}", 51L))
				.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "USER")
	void deleteByIdWhenHttpStatusForbidden() throws Exception {
		final GenreDto genreDto = createGenreDto();
		genreService.save(genreDto);
		mockMvc.perform(delete("/genres/{id}", 51L))
				.andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "Alex", roles = "ADMIN")
	void deleteByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(delete("/genres/{id}", NOT_FOUND_GENRE_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Genre with id " + NOT_FOUND_GENRE_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	private GenreDto createGenreDto() {
		GenreDto genreDto = new GenreDto();
		genreDto.setName(GENRE_NAME);

		return genreDto;
	}
}
