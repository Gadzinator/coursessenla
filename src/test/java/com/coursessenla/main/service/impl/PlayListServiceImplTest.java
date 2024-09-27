package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.exception.MovieNotFoundException;
import com.coursessenla.main.exception.PlayListNotFoundException;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.impl.PlayListRepositoryImpl;
import com.coursessenla.main.service.impl.config.ServiceTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
class PlayListServiceImplTest {

	private static final String UPDATE_PLAYLIST_NAME = "My playlist";

	@Mock
	private PlayListRepositoryImpl playListRepository;

	@Mock
	private GenericMapperImpl mapper;

	@InjectMocks
	private PlayListServiceImpl playListService;

	private static final long PLAYLIST_ID = 1;

	@Test
	void save() {
		final Playlist playlist = createPlaylist();
		final PlayListDto playlistDto = createPlaylistDto();

		when(mapper.mapToEntity(playlistDto, Playlist.class)).thenReturn(playlist);
		doNothing().when(playListRepository).save(playlist);

		playListService.save(playlistDto);

		verify(mapper).mapToEntity(playlistDto, Playlist.class);
		verify(playListRepository).save(playlist);
		assertEquals(playlist.getName(), playlistDto.getName());
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final Playlist playlist = createPlaylist();
		final PlayListDto playlistDto = createPlaylistDto();

		when(mapper.mapToEntity(playlistDto, Playlist.class)).thenReturn(playlist);
		doThrow(new RuntimeException("Database error")).when(playListRepository).save(playlist);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> playListService.save(playlistDto));

		assertEquals("Database error", exception.getMessage());
		verify(mapper).mapToEntity(playlistDto, Playlist.class);
		verify(playListRepository).save(playlist);
	}

	@Test
	void testFindByIdWhenPlaylistExist() {
		final Playlist playlist = createPlaylist();
		final PlayListDto playlistDto = createPlaylistDto();

		when(playListRepository.findById(PLAYLIST_ID)).thenReturn(Optional.of(playlist));
		when(mapper.mapToDto(playlist, PlayListDto.class)).thenReturn(playlistDto);

		final PlayListDto existPlayListDto = playListService.findById(PLAYLIST_ID);

		verify(playListRepository).findById(PLAYLIST_ID);
		verify(mapper).mapToDto(playlist, PlayListDto.class);
		assertEquals(playlistDto, existPlayListDto);
	}

	@Test
	void testFindByIdWhenPlaylistNotExist() {
		when(playListRepository.findById(PLAYLIST_ID)).thenReturn(Optional.empty());

		assertThrows(PlayListNotFoundException.class, () -> playListService.findById(PLAYLIST_ID));
	}

	@Test
	void testFindAllWhenListPlaylistsNotEmpty() {
		final Playlist firstPlaylist = createPlaylist();
		final Playlist secondPlaylist = createPlaylist();
		secondPlaylist.setId(2L);
		final PlayListDto firstPlaylistDto = createPlaylistDto();
		final PlayListDto secondPlaylistDto = createPlaylistDto();
		secondPlaylistDto.setId(2L);

		final List<Playlist> playlists = Arrays.asList(firstPlaylist, secondPlaylist);

		Pageable pageable = PageRequest.of(0, 10);

		Page<Playlist> playlistPage = new PageImpl<>(playlists, pageable, playlists.size());

		when(playListRepository.findAll(pageable)).thenReturn(playlistPage);
		when(mapper.mapToDto(firstPlaylist, PlayListDto.class)).thenReturn(firstPlaylistDto);
		when(mapper.mapToDto(secondPlaylist, PlayListDto.class)).thenReturn(secondPlaylistDto);

		Page<PlayListDto> actualPlayListDtoPage = playListService.findAll(pageable);

		verify(playListRepository).findAll(pageable);
		verify(mapper).mapToDto(firstPlaylist, PlayListDto.class);
		verify(mapper).mapToDto(secondPlaylist, PlayListDto.class);

		assertNotNull(actualPlayListDtoPage);
		assertEquals(2, actualPlayListDtoPage.getContent().size());
		assertTrue(actualPlayListDtoPage.getContent().contains(firstPlaylistDto));
		assertTrue(actualPlayListDtoPage.getContent().contains(secondPlaylistDto));
	}

	@Test
	void testFindAllWhenListPlaylistsEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(playListRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(PlayListNotFoundException.class, () -> playListService.findAll(pageable));
	}

	@Test
	void testUpdateWhenPlaylistFound() {
		final Playlist playlist = createPlaylist();
		final PlayListDto playlistDto = createPlaylistDto();
		playlistDto.setName(UPDATE_PLAYLIST_NAME);

		when(playListRepository.findById(PLAYLIST_ID)).thenReturn(Optional.of(playlist));
		when(mapper.mapToDto(any(Playlist.class), eq(PlayListDto.class))).thenReturn(playlistDto);
		when(mapper.mapToEntity(any(PlayListDto.class), eq(Playlist.class))).thenAnswer(invocation -> {
			PlayListDto dto = invocation.getArgument(0);
			playlist.setName(dto.getName());
			return playlist;
		});
		doNothing().when(playListRepository).update(any(Playlist.class));

		playListService.update(playlistDto);

		final ArgumentCaptor<Playlist> playlistArgumentCaptor = ArgumentCaptor.forClass(Playlist.class);
		verify(playListRepository).findById(PLAYLIST_ID);
		verify(mapper).mapToDto(any(Playlist.class), eq(PlayListDto.class));
		verify(mapper).mapToEntity(any(PlayListDto.class), eq(Playlist.class));
		verify(playListRepository).update(playlistArgumentCaptor.capture());
		final Playlist savedPlaylist = playlistArgumentCaptor.getValue();
		assertEquals(UPDATE_PLAYLIST_NAME, savedPlaylist.getName());
	}

	@Test
	void testUpdateWhenPlaylistNotFound() {
		final PlayListDto playlistDto = createPlaylistDto();

		when(playListRepository.findById(PLAYLIST_ID)).thenReturn(Optional.empty());

		assertThrows(PlayListNotFoundException.class, () -> playListService.update(playlistDto));
		verify(playListRepository).findById(PLAYLIST_ID);
		verify(playListRepository, never()).update(any(Playlist.class));
	}

	@Test
	void testDeleteByIdWhenPlaylistExist() {
		final Playlist playlist = createPlaylist();
		final PlayListDto playlistDto = createPlaylistDto();

		when(playListRepository.findById(PLAYLIST_ID)).thenReturn(Optional.of(playlist));
		when(mapper.mapToDto(any(Playlist.class), eq(PlayListDto.class))).thenReturn(playlistDto);
		doNothing().when(playListRepository).deleteById(PLAYLIST_ID);

		playListService.deleteById(PLAYLIST_ID);

		verify(playListRepository).findById(PLAYLIST_ID);
		verify(mapper).mapToDto(any(Playlist.class), eq(PlayListDto.class));
		verify(playListRepository).deleteById(PLAYLIST_ID);
	}

	@Test
	void testDeleteByIdWhenPlaylistNotExist() {
		when(playListRepository.findById(PLAYLIST_ID)).thenThrow(PlayListNotFoundException.class);

		assertThrows(PlayListNotFoundException.class, () -> playListService.deleteById(PLAYLIST_ID));
		verify(playListRepository, never()).deleteById(PLAYLIST_ID);
	}

	private Playlist createPlaylist() {
		Playlist playlist = new Playlist();
		playlist.setId(PLAYLIST_ID);
		playlist.setName("Test Playlist");

		return playlist;
	}

	private PlayListDto createPlaylistDto() {
		PlayListDto playListDto = new PlayListDto();
		playListDto.setId(PLAYLIST_ID);
		playListDto.setName("Test Playlist");

		return playListDto;
	}
}
