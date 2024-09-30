package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.domain.entity.CharacterInfo;
import com.coursessenla.main.domain.entity.CharacterInfoId;
import com.coursessenla.main.domain.entity.Movie;
import com.coursessenla.main.exception.ActorNotFoundException;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.impl.ActorRepositoryImpl;
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
import java.util.ArrayList;
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
class ActorServiceImplTest {

	private static final long ACTOR_ID = 1;
	private static final String ACTOR_NAME = "Leonardo DiCaprio";
	private static final String UPDATE_ACTOR_NAME = "Stallone";

	@Mock
	private ActorRepositoryImpl actorRepository;

	@Mock
	private GenericMapperImpl mapper;

	@InjectMocks
	private ActorServiceImpl actorService;

	@Test
	void testSave() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		final Actor actor = createActor();
		final ActorDto actorDto = createActorDto(characterInfoDto);

		when(mapper.mapToEntity(actorDto, Actor.class)).thenReturn(actor);
		doNothing().when(actorRepository).save(actor);

		actorService.save(actorDto);

		verify(mapper).mapToEntity(actorDto, Actor.class);
		verify(actorRepository).save(actor);
		assertEquals(actor.getName(), actorDto.getName());
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		final Actor actor = createActor();
		final ActorDto actorDto = createActorDto(characterInfoDto);

		when(mapper.mapToEntity(actorDto, Actor.class)).thenReturn(actor);
		doThrow(new RuntimeException("Database error")).when(actorRepository).save(actor);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> actorService.save(actorDto));

		assertEquals("Database error", exception.getMessage());
		verify(mapper).mapToEntity(actorDto, Actor.class);
		verify(actorRepository).save(actor);
	}

	@Test
	void testFindByIdWhenActorExist() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		final Actor actor = createActor();
		final ActorDto actorDto = createActorDto(characterInfoDto);

		when(actorRepository.findById(ACTOR_ID)).thenReturn(Optional.of(actor));
		when(mapper.mapToDto(actor, ActorDto.class)).thenReturn(actorDto);

		final ActorDto existActorDto = actorService.findById(ACTOR_ID);

		verify(actorRepository).findById(ACTOR_ID);
		verify(mapper).mapToDto(actor, ActorDto.class);
		assertEquals(actorDto, existActorDto);
	}

	@Test
	void testFindByIdWhenActorNotExist() {
		when(actorRepository.findById(ACTOR_ID)).thenReturn(Optional.empty());

		assertThrows(ActorNotFoundException.class, () -> actorService.findById(ACTOR_ID));
	}

	@Test
	void testFindByNameWhenActorExist() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		final Actor actor = createActor();
		final ActorDto actorDto = createActorDto(characterInfoDto);

		when(actorRepository.findByName(ACTOR_NAME)).thenReturn(Optional.of(actor));
		when(mapper.mapToDto(actor, ActorDto.class)).thenReturn(actorDto);

		final ActorDto actualeActorDto = actorService.findByName(ACTOR_NAME);

		verify(actorRepository).findByName(ACTOR_NAME);
		verify(mapper).mapToDto(actor, ActorDto.class);
		assertEquals(actorDto, actualeActorDto);
	}

	@Test
	void testFindByNameWhenActorNotExist() {
		when(actorRepository.findByName(ACTOR_NAME)).thenReturn(Optional.empty());

		assertThrows(ActorNotFoundException.class, () -> actorService.findByName(ACTOR_NAME));
	}

	@Test
	void testFindAllWhenListActorsNotEmpty() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		final Actor firstActor = createActor();
		final Actor secondActor = createActor();
		secondActor.setId(2L);
		final List<Actor> actorList = Arrays.asList(firstActor, secondActor);

		final ActorDto firstActorDto = createActorDto(characterInfoDto);
		final ActorDto secondActorDto = createActorDto(characterInfoDto);
		secondActorDto.setId(2L);

		// Создание pageable
		Pageable pageable = PageRequest.of(0, 2); // Параметры пагинации: страница 0, размер страницы 2

		// Создание страницы с актерами
		Page<Actor> actorPage = new PageImpl<>(actorList, pageable, actorList.size());

		// Мокаем поведение репозитория и mapper
		when(actorRepository.findAll(pageable)).thenReturn(actorPage);
		when(mapper.mapToDto(firstActor, ActorDto.class)).thenReturn(firstActorDto);
		when(mapper.mapToDto(secondActor, ActorDto.class)).thenReturn(secondActorDto);

		// Вызываем метод findAll с pageable
		Page<ActorDto> actualActorDtoPage = actorService.findAll(pageable);

		// Проверяем, что репозиторий и mapper были вызваны
		verify(actorRepository).findAll(pageable);
		verify(mapper).mapToDto(firstActor, ActorDto.class);
		verify(mapper).mapToDto(secondActor, ActorDto.class);

		// Проверяем количество элементов и их наличие
		assertEquals(2, actualActorDtoPage.getContent().size());
		assertTrue(actualActorDtoPage.getContent().contains(firstActorDto));
		assertTrue(actualActorDtoPage.getContent().contains(secondActorDto));
	}

	@Test
	void testFindAllWhenListActorsEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(actorRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(ActorNotFoundException.class, () -> actorService.findAll(pageable));
	}

	@Test
	void testUpdateWhenActorFound() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfo characterInfo = createCharacterInfo(characterInfoId);
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		final Actor actor = createActor();
		final ActorDto actorDto = createActorDto(characterInfoDto);
		actorDto.setName(UPDATE_ACTOR_NAME);

		when(actorRepository.findById(ACTOR_ID)).thenReturn(Optional.of(actor));
		when(mapper.mapToEntity(characterInfoDto, CharacterInfo.class)).thenReturn(characterInfo);
		when(mapper.mapToDto(any(Actor.class), eq(ActorDto.class))).thenReturn(actorDto);
		when(mapper.mapToEntity(any(ActorDto.class), eq(Actor.class))).thenAnswer(invocation -> {
			ActorDto dto = invocation.getArgument(0);
			actor.setName(dto.getName());
			return actor;
		});
		doNothing().when(actorRepository).update(any(Actor.class));

		actorService.update(actorDto);

		final ArgumentCaptor<Actor> actorArgumentCaptor = ArgumentCaptor.forClass(Actor.class);
		verify(actorRepository).findById(ACTOR_ID);
		verify(mapper).mapToEntity(any(CharacterInfoDto.class), eq(CharacterInfo.class));
		verify(mapper).mapToDto(any(Actor.class), eq(ActorDto.class));
		verify(mapper).mapToEntity(any(ActorDto.class), eq(Actor.class));
		verify(actorRepository).update(actorArgumentCaptor.capture());
		final Actor savedActor = actorArgumentCaptor.getValue();
		assertEquals(UPDATE_ACTOR_NAME, savedActor.getName());
	}

	@Test
	void testUpdateWhenActorNotFound() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		final ActorDto actorDto = createActorDto(characterInfoDto);

		when(actorRepository.findById(ACTOR_ID)).thenReturn(Optional.empty());

		assertThrows(ActorNotFoundException.class, () -> actorService.update(actorDto));
		verify(actorRepository).findById(ACTOR_ID);
		verify(actorRepository, never()).update(any(Actor.class));
	}

	@Test
	void testDeleteByIdWhenActorExist() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		final Actor actor = createActor();
		final ActorDto actorDto = createActorDto(characterInfoDto);

		when(actorRepository.findById(ACTOR_ID)).thenReturn(Optional.of(actor));
		when(mapper.mapToDto(any(Actor.class), eq(ActorDto.class))).thenReturn(actorDto);
		doNothing().when(actorRepository).deleteById(ACTOR_ID);

		actorService.deleteById(ACTOR_ID);

		verify(actorRepository).findById(ACTOR_ID);
		verify(mapper).mapToDto(any(Actor.class), eq(ActorDto.class));
		verify(actorRepository).deleteById(ACTOR_ID);
	}

	@Test
	void testDeleteByIdWhenActorNotExist() {
		when(actorRepository.findById(ACTOR_ID)).thenThrow(ActorNotFoundException.class);

		assertThrows(ActorNotFoundException.class, () -> actorService.deleteById(ACTOR_ID));
		verify(actorRepository, never()).deleteById(ACTOR_ID);
	}

	private Actor createActor() {
		Actor actor = new Actor();
		actor.setId(ACTOR_ID);
		actor.setName(ACTOR_NAME);
		actor.setBirthDate(LocalDate.parse("1974-11-11"));
		actor.setNationality("American");
		actor.setAwards("Oscar");

		return actor;
	}

	private CharacterInfo createCharacterInfo(CharacterInfoId characterInfoId) {
		CharacterInfo characterInfo = new CharacterInfo();
		characterInfo.setId(characterInfoId);
		characterInfo.setActor(new Actor());
		characterInfo.setCharacterName("Dom Cobb");
		characterInfo.setMovie(new Movie());

		return characterInfo;
	}

	private ActorDto createActorDto(CharacterInfoDto characterInfoDto) {
		List<CharacterInfoDto> characterInfoDtoList = new ArrayList<>();
		characterInfoDtoList.add(characterInfoDto);
		ActorDto actorDto = new ActorDto();
		actorDto.setId(ACTOR_ID);
		actorDto.setName(ACTOR_NAME);
		actorDto.setBirthDate("1974-11-11");
		actorDto.setNationality("American");
		actorDto.setAwards("Oscar");
		actorDto.setCharacterInfos(characterInfoDtoList);

		return actorDto;
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
