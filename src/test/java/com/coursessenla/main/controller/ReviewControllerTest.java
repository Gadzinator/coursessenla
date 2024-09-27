package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.dto.MovieDto;
import com.coursessenla.main.domain.dto.ReviewDto;
import com.coursessenla.main.domain.dto.UserDto;
import com.coursessenla.main.service.impl.ReviewServiceImpl;
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
class ReviewControllerTest {

	private static final long REVIEW_ID = 1;
	private static final long NOT_FOUND_REVIEW_ID = 51;
	private static final String GENRE_NAME = "Action";
	private static final String REVIEW_CONTENT = "Review content 1";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ReviewServiceImpl reviewService;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void testSaveWhenHttpStatusCreated() throws Exception {
		final GenreDto genreDto = createGenreDto();
		final MovieDto movieDto = createMovieDto(genreDto);
		final UserDto userDto = createUserDto();
		final ReviewDto reviewDto = createReviewDto(movieDto, userDto);
		mockMvc.perform(post("/reviews")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewDto)))
				.andExpect(status().isCreated());
	}

	@Test
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/reviews")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByIdWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/reviews/{id}", REVIEW_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(REVIEW_ID));
	}

	@Test
	void findByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/reviews/{id}", NOT_FOUND_REVIEW_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Review with id " + NOT_FOUND_REVIEW_ID + " was not found",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/reviews")
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
		final UserDto userDto = createUserDto();
		final ReviewDto reviewDto = createReviewDto(movieDto, userDto);
		reviewDto.setId(REVIEW_ID);
		mockMvc.perform(put("/reviews", reviewDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewDto)))
				.andExpect(status().isOk());
	}

	@Test
	void updateWhenActorNotFoundException() throws Exception {
		final GenreDto genreDto = createGenreDto();
		final MovieDto movieDto = createMovieDto(genreDto);
		final UserDto userDto = createUserDto();
		final ReviewDto reviewDto = createReviewDto(movieDto, userDto);
		reviewDto.setId(NOT_FOUND_REVIEW_ID);
		mockMvc.perform(put("/reviews")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(reviewDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Review with id " + NOT_FOUND_REVIEW_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void deleteByIdWhenHttpStatusNoContent() throws Exception {
		final GenreDto genreDto = createGenreDto();
		final MovieDto movieDto = createMovieDto(genreDto);
		final UserDto userDto = createUserDto();
		final ReviewDto reviewDto = createReviewDto(movieDto, userDto);
		reviewService.save(reviewDto);
		mockMvc.perform(delete("/reviews/{id}", 51L))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(delete("/reviews/{id}", NOT_FOUND_REVIEW_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Review with id " + NOT_FOUND_REVIEW_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	private ReviewDto createReviewDto(MovieDto movieDto, UserDto userDto) {
		ReviewDto reviewDto = new ReviewDto();
		reviewDto.setContent("This is a great movie!");
		reviewDto.setMovie(movieDto);
		reviewDto.setRating(8.5);
		reviewDto.setUser(userDto);

		return reviewDto;
	}

	private MovieDto createMovieDto(GenreDto genreDto) {
		MovieDto movieDto = new MovieDto();
		List<GenreDto> genreDtoList = new ArrayList<>();
		genreDtoList.add(genreDto);
		movieDto.setId(1L);
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

	private UserDto createUserDto() {
		UserDto userDto = new UserDto();
		userDto.setId(1L);
		userDto.setEmail("client1@example.com");

		return userDto;
	}
}
