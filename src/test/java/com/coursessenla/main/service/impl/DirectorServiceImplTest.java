package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.exception.DirectorNotFoundException;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.impl.DirectorRepositoryImpl;
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

import java.time.LocalDate;
import java.util.Arrays;
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
class DirectorServiceImplTest {

	private static final long DIRECTOR_ID = 1;
	private static final String DIRECTOR_NAME = "Christopher Nolan";
	private static final String UPDATE_DIRECTOR_NAME = "Chris";

	@Mock
	private DirectorRepositoryImpl directorRepository;

	@Mock
	private GenericMapperImpl mapper;

	@InjectMocks
	private DirectorServiceImpl directorService;

	@Test
	void testSave() {
		final Director director = createDirector();
		final DirectorDto directorDto = createDirectorDto();

		when(mapper.mapToEntity(directorDto, Director.class)).thenReturn(director);
		doNothing().when(directorRepository).save(director);

		directorService.save(directorDto);

		verify(mapper).mapToEntity(directorDto, Director.class);
		verify(directorRepository).save(director);
		assertEquals(director.getName(), directorDto.getName());
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final Director director = createDirector();
		final DirectorDto directorDto = createDirectorDto();

		when(mapper.mapToEntity(directorDto, Director.class)).thenReturn(director);
		doThrow(new RuntimeException("Database error")).when(directorRepository).save(director);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> directorService.save(directorDto));

		assertEquals("Database error", exception.getMessage());
		verify(mapper).mapToEntity(directorDto, Director.class);
		verify(directorRepository).save(director);
	}

	@Test
	void testFindByIdWhenDirectorExist() {
		final Director director = createDirector();
		final DirectorDto directorDto = createDirectorDto();

		when(directorRepository.findById(DIRECTOR_ID)).thenReturn(Optional.of(director));
		when(mapper.mapToDto(director, DirectorDto.class)).thenReturn(directorDto);

		final DirectorDto existDirectorDto = directorService.findById(DIRECTOR_ID);

		verify(directorRepository).findById(DIRECTOR_ID);
		verify(mapper).mapToDto(director, DirectorDto.class);
		assertEquals(directorDto, existDirectorDto);
	}

	@Test
	void testFindByIdWhenDirectorNotExist() {
		when(directorRepository.findById(DIRECTOR_ID)).thenReturn(Optional.empty());

		assertThrows(DirectorNotFoundException.class, () -> directorService.findById(DIRECTOR_ID));
	}

	@Test
	void testFindByNameWhenDirectorExist() {
		final Director director = createDirector();
		final DirectorDto directorDto = createDirectorDto();

		when(directorRepository.findByName(DIRECTOR_NAME)).thenReturn(Optional.of(director));
		when(mapper.mapToDto(director, DirectorDto.class)).thenReturn(directorDto);

		final DirectorDto actualeDirectorDto = directorService.findByName(DIRECTOR_NAME);

		verify(directorRepository).findByName(DIRECTOR_NAME);
		verify(mapper).mapToDto(director, DirectorDto.class);
		assertEquals(directorDto, actualeDirectorDto);
	}

	@Test
	void testFindByNameWhenDirectorNotExist() {
		when(directorRepository.findByName(DIRECTOR_NAME)).thenReturn(Optional.empty());

		assertThrows(DirectorNotFoundException.class, () -> directorService.findByName(DIRECTOR_NAME));
	}

	@Test
	void testFindAllWhenListDirectorsNotEmpty() {
		final Director firstDirector = createDirector();
		final Director secondDirector = createDirector();
		secondDirector.setId(2L);

		final DirectorDto firstDirectorDto = createDirectorDto();
		final DirectorDto secondDirectorDto = createDirectorDto();
		secondDirectorDto.setId(2L);

		List<Director> directorList = Arrays.asList(firstDirector, secondDirector);

		Pageable pageable = PageRequest.of(0, 2);
		Page<Director> directorPage = new PageImpl<>(directorList, pageable, directorList.size());

		when(directorRepository.findAll(pageable)).thenReturn(directorPage);
		when(mapper.mapToDto(firstDirector, DirectorDto.class)).thenReturn(firstDirectorDto);
		when(mapper.mapToDto(secondDirector, DirectorDto.class)).thenReturn(secondDirectorDto);

		final Page<DirectorDto> actualDirectorDtoPage = directorService.findAll(pageable);

		verify(directorRepository).findAll(pageable);
		verify(mapper).mapToDto(firstDirector, DirectorDto.class);
		verify(mapper).mapToDto(secondDirector, DirectorDto.class);
		assertEquals(2, actualDirectorDtoPage.getContent().size());
		assertTrue(actualDirectorDtoPage.getContent().contains(firstDirectorDto));
		assertTrue(actualDirectorDtoPage.getContent().contains(secondDirectorDto));
		assertEquals(2, actualDirectorDtoPage.getTotalElements());
	}

	@Test
	void testFindAllWhenListDirectorsEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(directorRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(DirectorNotFoundException.class, () -> directorService.findAll(pageable));
	}

	@Test
	void testUpdateWhenDirectorExist() {
		final Director director = createDirector();
		final DirectorDto directorDto = createDirectorDto();
		directorDto.setName(UPDATE_DIRECTOR_NAME);


		when(directorRepository.findById(DIRECTOR_ID)).thenReturn(Optional.of(director));
		when(mapper.mapToDto(any(Director.class), eq(DirectorDto.class))).thenReturn(directorDto);
		when(mapper.mapToEntity(any(DirectorDto.class), eq(Director.class))).thenAnswer(invocation -> {
			DirectorDto dto = invocation.getArgument(0);
			director.setName(dto.getName());
			return director;
		});
		doNothing().when(directorRepository).update(any(Director.class));

		directorService.update(directorDto);

		final ArgumentCaptor<Director> directorArgumentCaptor = ArgumentCaptor.forClass(Director.class);
		verify(directorRepository).findById(DIRECTOR_ID);
		verify(mapper).mapToDto(any(Director.class), eq(DirectorDto.class));
		verify(mapper).mapToEntity(any(DirectorDto.class), eq(Director.class));
		verify(directorRepository).update(directorArgumentCaptor.capture());
		final Director savedDirector = directorArgumentCaptor.getValue();
		assertEquals(UPDATE_DIRECTOR_NAME, savedDirector.getName());
	}

	@Test
	void testUpdateWhenDirectorNotExist() {
		final DirectorDto directorDto = createDirectorDto();

		when(directorRepository.findById(DIRECTOR_ID)).thenReturn(Optional.empty());

		assertThrows(DirectorNotFoundException.class, () -> directorService.update(directorDto));
		verify(directorRepository).findById(DIRECTOR_ID);
		verify(directorRepository, never()).update(any(Director.class));
	}

	@Test
	void testDeleteByIdWhenDirectorExist() {
		final Director director = createDirector();
		final DirectorDto directorDto = createDirectorDto();

		when(directorRepository.findById(DIRECTOR_ID)).thenReturn(Optional.of(director));
		when(mapper.mapToDto(any(Director.class), eq(DirectorDto.class))).thenReturn(directorDto);
		doNothing().when(directorRepository).deleteById(DIRECTOR_ID);

		directorService.deleteById(DIRECTOR_ID);

		verify(directorRepository).findById(DIRECTOR_ID);
		verify(mapper).mapToDto(any(Director.class), eq(DirectorDto.class));
		verify(directorRepository).deleteById(DIRECTOR_ID);
	}

	@Test
	void testDeleteByIdWhenDirectorNotExist() {
		when(directorRepository.findById(DIRECTOR_ID)).thenThrow(DirectorNotFoundException.class);

		assertThrows(DirectorNotFoundException.class, () -> directorService.deleteById(DIRECTOR_ID));
		verify(directorRepository, never()).deleteById(DIRECTOR_ID);
	}

	private Director createDirector() {
		Director director = new Director();
		director.setId(DIRECTOR_ID);
		director.setName(DIRECTOR_NAME);
		director.setBirthDate(LocalDate.parse("1970-10-17"));
		director.setNationality("British-American");
		director.setAwards("Oscar, Golden Globe");

		return director;
	}

	private DirectorDto createDirectorDto() {
		DirectorDto directorDto = new DirectorDto();
		directorDto.setId(DIRECTOR_ID);
		directorDto.setName(DIRECTOR_NAME);
		directorDto.setBirthDate("1970-10-17");
		directorDto.setNationality("British-American");
		directorDto.setAwards("Oscar, Golden Globe");

		return directorDto;
	}
}
