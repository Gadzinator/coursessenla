package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.exception.GenreNotFoundException;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.impl.GenreRepositoryImpl;
import com.coursessenla.main.service.impl.config.ServiceTestConfiguration;
import org.junit.jupiter.api.Assertions;
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
class GenreServiceImplTest {

	private static final long GENRE_ID = 1;
	private static final String ACTION_GENRE_NAME = "Action";
	private static final String UPDATE_GENRE_NAME = "Comedy";

	@Mock
	private GenreRepositoryImpl genreRepository;

	@Mock
	private GenericMapperImpl mapper;

	@InjectMocks
	private GenreServiceImpl genreService;

	@Test
	void save() {
		final Genre genre = createGenre();
		final GenreDto genreDto = createGenreDto();

		when(mapper.mapToEntity(genreDto, Genre.class)).thenReturn(genre);
		doNothing().when(genreRepository).save(genre);

		genreService.save(genreDto);

		verify(mapper).mapToEntity(genreDto, Genre.class);
		verify(genreRepository).save(genre);
		assertEquals(genre.getName(), genreDto.getName());
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final Genre genre = createGenre();
		final GenreDto genreDto = createGenreDto();

		when(mapper.mapToEntity(genreDto, Genre.class)).thenReturn(genre);
		doThrow(new RuntimeException("Database error")).when(genreRepository).save(genre);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> genreService.save(genreDto));

		assertEquals("Database error", exception.getMessage());
		verify(mapper).mapToEntity(genreDto, Genre.class);
		verify(genreRepository).save(genre);
	}


	@Test
	void testFindByIdWhenGenreExist() {
		final Genre genre = createGenre();
		final GenreDto genreDto = createGenreDto();

		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
		when(mapper.mapToDto(genre, GenreDto.class)).thenReturn(genreDto);

		final GenreDto existGenreDto = genreService.findById(GENRE_ID);

		verify(genreRepository).findById(GENRE_ID);
		verify(mapper).mapToDto(genre, GenreDto.class);
		assertEquals(genreDto, existGenreDto);
	}

	@Test
	void testFindByIdWhenGenreNotExist() {
		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());

		assertThrows(GenreNotFoundException.class, () -> genreService.findById(GENRE_ID));
	}

	@Test
	void testFindByNameWhenGenreExist() {
		final Genre genre = createGenre();
		final GenreDto genreDto = createGenreDto();

		when(genreRepository.findByName(ACTION_GENRE_NAME)).thenReturn(Optional.of(genre));
		when(mapper.mapToDto(genre, GenreDto.class)).thenReturn(genreDto);

		final GenreDto actualeGenreDto = genreService.findByName(ACTION_GENRE_NAME);

		verify(genreRepository).findByName(ACTION_GENRE_NAME);
		verify(mapper).mapToDto(genre, GenreDto.class);
		assertEquals(genreDto, actualeGenreDto);
	}

	@Test
	void testFindByNameWhenGenreNotExist() {
		when(genreRepository.findByName(ACTION_GENRE_NAME)).thenReturn(Optional.empty());

		assertThrows(GenreNotFoundException.class, () -> genreService.findByName(ACTION_GENRE_NAME));
	}

	@Test
	void testFindAllWhenListGenresNotEmpty() {
		final Genre firstGenre = createGenre();
		final Genre secondGenre = createGenre();
		secondGenre.setId(2L);
		final GenreDto firstGenreDto = createGenreDto();
		final GenreDto secondGenreDto = createGenreDto();
		secondGenreDto.setId(2L);

		final List<Genre> genreList = Arrays.asList(firstGenre, secondGenre);

		Pageable pageable = PageRequest.of(0, 10);

		Page<Genre> genrePage = new PageImpl<>(genreList, pageable, genreList.size());
		when(genreRepository.findAll(pageable)).thenReturn(genrePage);

		when(mapper.mapToDto(firstGenre, GenreDto.class)).thenReturn(firstGenreDto);
		when(mapper.mapToDto(secondGenre, GenreDto.class)).thenReturn(secondGenreDto);

		Page<GenreDto> actualGenreDtoPage = genreService.findAll(pageable);

		verify(genreRepository).findAll(pageable);
		verify(mapper).mapToDto(firstGenre, GenreDto.class);
		verify(mapper).mapToDto(secondGenre, GenreDto.class);
		Assertions.assertNotNull(actualGenreDtoPage);
		assertEquals(2, actualGenreDtoPage.getContent().size());
		assertTrue(actualGenreDtoPage.getContent().contains(firstGenreDto));
		assertTrue(actualGenreDtoPage.getContent().contains(secondGenreDto));
	}

	@Test
	void testFindAllWhenListGenresEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(genreRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(GenreNotFoundException.class, () -> genreService.findAll(pageable));
	}

	@Test
	void testUpdateWhenGenreFound() {
		final Genre genre = createGenre();
		final GenreDto genreDto = createGenreDto();
		genreDto.setName(UPDATE_GENRE_NAME);

		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
		when(mapper.mapToDto(any(Genre.class), eq(GenreDto.class))).thenReturn(genreDto);
		when(mapper.mapToEntity(any(GenreDto.class), eq(Genre.class))).thenAnswer(invocation -> {
			GenreDto dto = invocation.getArgument(0);
			genre.setName(dto.getName());
			return genre;
		});
		doNothing().when(genreRepository).update(any(Genre.class));

		genreService.update(genreDto);

		final ArgumentCaptor<Genre> genreArgumentCaptor = ArgumentCaptor.forClass(Genre.class);
		verify(genreRepository).findById(GENRE_ID);
		verify(mapper).mapToDto(any(Genre.class), eq(GenreDto.class));
		verify(mapper).mapToEntity(any(GenreDto.class), eq(Genre.class));
		verify(genreRepository).update(genreArgumentCaptor.capture());
		final Genre savedGenre = genreArgumentCaptor.getValue();
		assertEquals(UPDATE_GENRE_NAME, savedGenre.getName());
	}

	@Test
	void testUpdateWhenGenreNotFound() {
		final GenreDto genreDto = createGenreDto();

		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.empty());

		assertThrows(GenreNotFoundException.class, () -> genreService.update(genreDto));
		verify(genreRepository).findById(GENRE_ID);
		verify(genreRepository, never()).update(any(Genre.class));
	}

	@Test
	void testDeleteByIdWhenGenreExist() {
		final Genre genre = createGenre();
		final GenreDto genreDto = createGenreDto();

		when(genreRepository.findById(GENRE_ID)).thenReturn(Optional.of(genre));
		when(mapper.mapToDto(any(Genre.class), eq(GenreDto.class))).thenReturn(genreDto);
		doNothing().when(genreRepository).deleteById(GENRE_ID);

		genreService.deleteById(GENRE_ID);

		verify(genreRepository).findById(GENRE_ID);
		verify(mapper).mapToDto(any(Genre.class), eq(GenreDto.class));
		verify(genreRepository).deleteById(GENRE_ID);
	}

	@Test
	void testDeleteByIdWhenGenreNotExist() {
		when(genreRepository.findById(GENRE_ID)).thenThrow(GenreNotFoundException.class);

		assertThrows(GenreNotFoundException.class, () -> genreService.deleteById(GENRE_ID));
		verify(genreRepository, never()).deleteById(GENRE_ID);
	}

	private Genre createGenre() {
		Genre genre = new Genre();
		genre.setId(GENRE_ID);
		genre.setName(ACTION_GENRE_NAME);
		genre.setMovies(Collections.emptyList());

		return genre;
	}

	private GenreDto createGenreDto() {
		GenreDto genreDto = new GenreDto();
		genreDto.setId(GENRE_ID);
		genreDto.setName(ACTION_GENRE_NAME);

		return genreDto;
	}
}
