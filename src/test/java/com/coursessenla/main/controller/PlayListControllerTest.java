package com.coursessenla.main.controller;

import com.coursessenla.main.config.HibernateConfig;
import com.coursessenla.main.config.LiquibaseConfig;
import com.coursessenla.main.controller.config.WebMvcConfig;
import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.service.PlayListService;
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
class PlayListControllerTest {

	private static final long PLAYLIST_ID = 1;
	private static final long NOT_FOUND_PLAYLIST_ID = 51;
	private static final String PLAYLIST_NAME = "Playlist1";

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PlayListService playListService;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	void testSaveWhenHttpStatusCreated() throws Exception {
		final PlayListDto playlistDto = createPlaylistDto();
		mockMvc.perform(post("/playlists")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(playlistDto)))
				.andExpect(status().isCreated());
	}

	@Test
	void testSaveWhenHttpStatusBadRequest() throws Exception {
		mockMvc.perform(post("/playlists")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(null)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByIdWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/playlists/{id}", PLAYLIST_ID))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(PLAYLIST_ID));
	}

	@Test
	void findByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(get("/playlists/{id}", NOT_FOUND_PLAYLIST_ID))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Playlist with id " + NOT_FOUND_PLAYLIST_ID + " was not found",
						Objects.requireNonNull(result.getResolvedException()).getMessage()));
	}

	@Test
	void findAllWhenHttpStatusOk() throws Exception {
		mockMvc.perform(get("/playlists")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(50))
				.andExpect(jsonPath("$.totalElements").value(50));
	}

	@Test
	void updateWhenHttpStatusOk() throws Exception {
		final PlayListDto playlistDto = createPlaylistDto();
		playlistDto.setId(PLAYLIST_ID);
		mockMvc.perform(put("/playlists", playlistDto)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(playlistDto)))
				.andExpect(status().isOk());
	}

	@Test
	void updateWhenActorNotFoundException() throws Exception {
		final PlayListDto playlistDto = createPlaylistDto();
		playlistDto.setId(NOT_FOUND_PLAYLIST_ID);
		mockMvc.perform(put("/playlists")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(playlistDto)))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Playlist with id " + NOT_FOUND_PLAYLIST_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	@Test
	void deleteByIdWhenHttpStatusNoContent() throws Exception {
		final PlayListDto playlistDto = createPlaylistDto();
		playListService.save(playlistDto);
		mockMvc.perform(delete("/playlists/{id}", 51L))
				.andExpect(status().isNoContent());
	}

	@Test
	void deleteByIdWhenActorNotFoundException() throws Exception {
		mockMvc.perform(delete("/playlists/{id}", NOT_FOUND_PLAYLIST_ID)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertEquals("Playlist with id " + NOT_FOUND_PLAYLIST_ID + " was not found",
						Objects.requireNonNull(Objects.requireNonNull(result.getResolvedException()).getMessage())));
	}

	private PlayListDto createPlaylistDto() {
		PlayListDto playListDto = new PlayListDto();
		playListDto.setName("Test Playlist");
		playListDto.setUserId(1L);

		return playListDto;
	}
}
