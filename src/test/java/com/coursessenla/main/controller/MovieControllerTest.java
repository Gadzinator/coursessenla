package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.service.MovieService;
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

import java.util.ArrayList;
import java.util.List;
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
class MovieControllerTest {

	private static final long MOVIE_ID = 1;
	private static final long NOT_FOUND_MOVIE_ID = 52;
	private static final String GENRE_NAME = "Action";
	private static final String MOVIE_TITLE = "title1";
	private static final String NOT_FOUND_MOVIE_TITLE = "title55";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MovieService movieService;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void testSaveWhenHttpStatusCreated() throws Exception {
		final GenreDto genreDto = createGenreDto();
		final MovieDto movieDto = createMovieDto(genreDto);
		mockMvc.perform(post("/movies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(movieDto)))
				.andExpect(status().isCreated());
	}

	@Test
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/movies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByIdWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/movies/id/{id}", MOVIE_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(MOVIE_ID))
				.andExpect(jsonPath("$.title").value(MOVIE_TITLE));
	}

	@Test
	void findByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/movies/id/{id}", NOT_FOUND_MOVIE_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Movie with id " + NOT_FOUND_MOVIE_ID + " was not found",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	void findByGenreNameWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/movies/{genreName}", GENRE_NAME))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value(MOVIE_ID))
				.andExpect(jsonPath("$[0].title").value(MOVIE_TITLE));
	}

	@Test
	void findByGenreNameWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/movies/{genreName}", NOT_FOUND_MOVIE_TITLE))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("No movies were found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/movies")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(50))
				.andExpect(jsonPath("$.totalElements").value(50));
	}

	@Test
	void updateWhenHttpStatusOk() throws Exception {
		final GenreDto genreDto = createGenreDto();
		final MovieDto movieDto = createMovieDto(genreDto);
		movieDto.setId(MOVIE_ID);
		mockMvc.perform(put("/movies", movieDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(movieDto)))
				.andExpect(status().isOk());
	}

	@Test
	void updateWhenActorNotFoundException() throws Exception {
		final GenreDto genreDto = createGenreDto();
		genreDto.setId(NOT_FOUND_MOVIE_ID);
		mockMvc.perform(put("/movies")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(genreDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Movie with id " + NOT_FOUND_MOVIE_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void deleteByIdWhenHttpStatusNoContent() throws Exception {
		final GenreDto genreDto = createGenreDto();
		final MovieDto movieDto = createMovieDto(genreDto);
		movieService.save(movieDto);
		mockMvc.perform(delete("/movies/{id}", 51L))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(delete("/movies/{id}", NOT_FOUND_MOVIE_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Movie with id " + NOT_FOUND_MOVIE_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	private MovieDto createMovieDto(GenreDto genreDto) {
		MovieDto movieDto = new MovieDto();
		List<GenreDto> genreDtoList = new ArrayList<>();
		genreDtoList.add(genreDto);
		movieDto.setTitle("Inception");
		movieDto.setReleaseDate("2017-10-16");
		movieDto.setGenres(genreDtoList);
		movieDto.setDescription("A mind-bending thriller where dreams are a reality.");
		movieDto.setRating(9.5);
		movieDto.setDirectorDtoList(new ArrayList<>());

		return movieDto;
	}

	private GenreDto createGenreDto() {
		GenreDto genreDto = new GenreDto();
		genreDto.setId(1L);
		genreDto.setName(GENRE_NAME);

		return genreDto;
	}
}
