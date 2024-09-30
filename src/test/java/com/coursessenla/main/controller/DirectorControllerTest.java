package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.service.DirectorService;
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
class DirectorControllerTest {

	private static final long DIRECTOR_ID = 1;
	private static final String DIRECTOR_NAME = "Director 1";
	private static final long NOT_FOUND_DIRECTOR_ID = 51;
	private static final String NOT_FOUND_DIRECTOR_NAME = "Director 51";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private DirectorService directorService;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void testSaveWhenHttpStatusCreated() throws Exception {
		final DirectorDto directorDto = createDirectorDto();
		mockMvc.perform(post("/directors")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(directorDto)))
				.andExpect(status().isCreated());
	}

	@Test
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/directors")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByIdWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/directors/id/{id}", DIRECTOR_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(DIRECTOR_ID))
				.andExpect(jsonPath("$.name").value(DIRECTOR_NAME));
	}

	@Test
	void findByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/directors/id/{id}", NOT_FOUND_DIRECTOR_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Director with id " + NOT_FOUND_DIRECTOR_ID + " was not found",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	void findByNameWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/directors/{name}", DIRECTOR_NAME))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(DIRECTOR_ID))
				.andExpect(jsonPath("$.name").value(DIRECTOR_NAME));
	}

	@Test
	void findByNameWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/directors/{name}", NOT_FOUND_DIRECTOR_NAME))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Director with name " + NOT_FOUND_DIRECTOR_NAME + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void findAllWhenHttpStatusOk() throws Exception {
			mockMvc.perform(get("/directors")
							.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.content").isArray())
					.andExpect(jsonPath("$.content.length()").value(50))
					.andExpect(jsonPath("$.totalElements").value(50));
	}

	@Test
	void updateWhenHttpStatusOk() throws Exception {
		final DirectorDto directorDto = createDirectorDto();
		directorDto.setId(DIRECTOR_ID);
		mockMvc.perform(put("/directors", directorDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(directorDto)))
				.andExpect(status().isOk());
	}

	@Test
	void updateWhenActorNotFoundException() throws Exception {
		final DirectorDto directorDto = createDirectorDto();
		directorDto.setId(NOT_FOUND_DIRECTOR_ID);
		mockMvc.perform(put("/directors")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(directorDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Director with id " + NOT_FOUND_DIRECTOR_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void deleteByIdWhenHttpStatusNoContent() throws Exception {
		final DirectorDto directorDto = createDirectorDto();
		directorService.save(directorDto);
		mockMvc.perform(delete("/directors/{id}", 51L))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(delete("/directors/{id}", NOT_FOUND_DIRECTOR_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Director with id " + NOT_FOUND_DIRECTOR_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	private DirectorDto createDirectorDto() {
		DirectorDto directorDto = new DirectorDto();
		directorDto.setName(DIRECTOR_NAME);
		directorDto.setBirthDate("1970-10-17");
		directorDto.setNationality("British-American");
		directorDto.setAwards("Oscar, Golden Globe");

		return directorDto;
	}
}
