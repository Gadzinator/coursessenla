package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.domain.entity.CharacterInfo;
import com.coursessenla.main.domain.entity.CharacterInfoId;
import com.coursessenla.main.exception.CharacterInfoNotFoundException;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.impl.CharacterInfoRepositoryImpl;
import com.coursessenla.main.service.ActorService;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
class CharacterInfoServiceImplTest {

	private final static String UPDATE_CHARACTER_NAME = "Doom";
	private final static long ACTOR_ID = 1;

	@Mock
	private CharacterInfoRepositoryImpl characterInfoRepository;

	@Mock
	private GenericMapperImpl mapper;

	@Mock
	private ActorService actorService;

	@InjectMocks
	private CharacterInfoServiceImpl characterInfoService;

	@Test
	void testSave() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfo characterInfo = createCharacterInfo(characterInfoId);
		Actor actor = mock(Actor.class);
		when(actor.getId()).thenReturn(1L);
		characterInfo.setActor(actor);
		final ActorDto actorDto = createActorDto();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);

		when(mapper.mapToEntity(characterInfoDto, CharacterInfo.class)).thenReturn(characterInfo);
		when(actorService.findById(ACTOR_ID)).thenReturn(actorDto);
		when(mapper.mapToEntity(actorDto, Actor.class)).thenReturn(actor);
		when(characterInfoRepository.findById(characterInfoId)).thenReturn(Optional.of(characterInfo));
		doNothing().when(characterInfoRepository).save(characterInfo);

		characterInfoService.save(characterInfoDto);

		verify(mapper).mapToEntity(characterInfoDto, CharacterInfo.class);
		verify(actorService).findById(ACTOR_ID);
		verify(mapper).mapToEntity(actorDto, Actor.class);
		verify(characterInfoRepository).findById(characterInfoId);
		verify(characterInfoRepository).save(characterInfo);
		assertEquals(characterInfo.getId(), characterInfoDto.getId());
		assertEquals(characterInfo.getCharacterName(), characterInfoDto.getCharacterName());
	}

	@Test
	void testSaveWhenRepositoryThrowsException() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfo characterInfo = createCharacterInfo(characterInfoId);
		Actor actor = mock(Actor.class);
		when(actor.getId()).thenReturn(1L);
		characterInfo.setActor(actor);
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);

		when(mapper.mapToEntity(characterInfoDto, CharacterInfo.class)).thenReturn(characterInfo);
		doThrow(new RuntimeException("Database error")).when(characterInfoRepository).save(characterInfo);
		RuntimeException exception = assertThrows(RuntimeException.class, () -> characterInfoService.save(characterInfoDto));

		assertEquals("Database error", exception.getMessage());
		verify(mapper).mapToEntity(characterInfoDto, CharacterInfo.class);
		verify(characterInfoRepository).save(characterInfo);
	}

	@Test
	void testFindByIdWhenCharacterInfoExist() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfo characterInfo = createCharacterInfo(characterInfoId);
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);

		when(characterInfoRepository.findById(characterInfoId)).thenReturn(Optional.of(characterInfo));
		when(mapper.mapToDto(characterInfo, CharacterInfoDto.class)).thenReturn(characterInfoDto);

		final CharacterInfoDto existCharacterInfoDto = characterInfoService.findById(characterInfoId);

		verify(characterInfoRepository).findById(characterInfoId);
		verify(mapper).mapToDto(characterInfo, CharacterInfoDto.class);
		assertEquals(characterInfoDto, existCharacterInfoDto);
	}

	@Test
	void testFindByIdWhenCharacterInfoNotExist() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();

		when(characterInfoRepository.findById(characterInfoId)).thenReturn(Optional.empty());

		assertThrows(CharacterInfoNotFoundException.class, () -> characterInfoService.findById(characterInfoId));
	}

	@Test
	void testFindAllWhenListCharacterInfosNotEmpty() {
		final CharacterInfoId firstCharacterInfoId = createCharacterInfoId();
		final CharacterInfoId secondCharacterInfoId = createCharacterInfoId();
		secondCharacterInfoId.setMovieId(2L);
		secondCharacterInfoId.setActorId(2L);

		final CharacterInfo firstCharacterInfo = createCharacterInfo(firstCharacterInfoId);
		final CharacterInfo secondCharacterInfo = createCharacterInfo(secondCharacterInfoId);

		final CharacterInfoDto firstCharacterInfoDto = createCharacterInfoDto(firstCharacterInfoId);
		final CharacterInfoDto secondCharacterInfoDto = createCharacterInfoDto(secondCharacterInfoId);

		List<CharacterInfo> characterInfoList = Arrays.asList(firstCharacterInfo, secondCharacterInfo);
		Page<CharacterInfo> characterInfoPage = new PageImpl<>(characterInfoList);

		Pageable pageable = PageRequest.of(0, 2);

		when(characterInfoRepository.findAll(pageable)).thenReturn(characterInfoPage);
		when(mapper.mapToDto(firstCharacterInfo, CharacterInfoDto.class)).thenReturn(firstCharacterInfoDto);
		when(mapper.mapToDto(secondCharacterInfo, CharacterInfoDto.class)).thenReturn(secondCharacterInfoDto);

		final Page<CharacterInfoDto> actualCharacterInfoDtoPage = characterInfoService.findAll(pageable);

		verify(characterInfoRepository).findAll(pageable);
		verify(mapper).mapToDto(firstCharacterInfo, CharacterInfoDto.class);
		verify(mapper).mapToDto(secondCharacterInfo, CharacterInfoDto.class);
		assertEquals(2, actualCharacterInfoDtoPage.getContent().size());
		assertTrue(actualCharacterInfoDtoPage.getContent().contains(firstCharacterInfoDto));
		assertTrue(actualCharacterInfoDtoPage.getContent().contains(secondCharacterInfoDto));
		assertEquals(2, actualCharacterInfoDtoPage.getTotalElements());
	}

	@Test
	void testFindAllWhenListCharacterInfosEmpty() {
		Pageable pageable = PageRequest.of(0, 10);

		when(characterInfoRepository.findAll(pageable)).thenReturn(Page.empty());

		assertThrows(CharacterInfoNotFoundException.class, () -> characterInfoService.findAll(pageable));
	}

	@Test
	void testUpdateWhenCharacterInfoFound() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfo characterInfo = createCharacterInfo(characterInfoId);
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);
		characterInfoDto.setCharacterName(UPDATE_CHARACTER_NAME);


		when(characterInfoRepository.findById(characterInfoId)).thenReturn(Optional.of(characterInfo));
		when(mapper.mapToDto(any(CharacterInfo.class), eq(CharacterInfoDto.class))).thenReturn(characterInfoDto);
		when(mapper.mapToEntity(any(CharacterInfoDto.class), eq(CharacterInfo.class))).thenAnswer(invocation -> {
			CharacterInfoDto dto = invocation.getArgument(0);
			characterInfo.setCharacterName(dto.getCharacterName());
			return characterInfo;
		});
		doNothing().when(characterInfoRepository).update(any(CharacterInfo.class));

		characterInfoService.update(characterInfoDto);

		final ArgumentCaptor<CharacterInfo> characterInfoArgumentCaptor = ArgumentCaptor.forClass(CharacterInfo.class);
		verify(characterInfoRepository).findById(characterInfoId);
		verify(mapper).mapToDto(any(CharacterInfo.class), eq(CharacterInfoDto.class));
		verify(mapper).mapToEntity(any(CharacterInfoDto.class), eq(CharacterInfo.class));
		verify(characterInfoRepository).update(characterInfoArgumentCaptor.capture());
		final CharacterInfo savedCharacterInfo = characterInfoArgumentCaptor.getValue();
		assertEquals(UPDATE_CHARACTER_NAME, savedCharacterInfo.getCharacterName());
	}

	@Test
	void testUpdateWhenCharacterInfoNotFound() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);

		when(characterInfoRepository.findById(characterInfoId)).thenReturn(Optional.empty());

		assertThrows(CharacterInfoNotFoundException.class, () -> characterInfoService.update(characterInfoDto));

		verify(characterInfoRepository).findById(characterInfoId);
		verify(characterInfoRepository, never()).update(any(CharacterInfo.class));
	}

	@Test
	void testDeleteByIdWhenCharacterInfoExist() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();
		final CharacterInfo characterInfo = createCharacterInfo(characterInfoId);
		final CharacterInfoDto characterInfoDto = createCharacterInfoDto(characterInfoId);

		when(characterInfoRepository.findById(characterInfoId)).thenReturn(Optional.of(characterInfo));
		when(mapper.mapToDto(any(CharacterInfo.class), eq(CharacterInfoDto.class))).thenReturn(characterInfoDto);
		doNothing().when(characterInfoRepository).deleteById(characterInfoId);

		characterInfoService.deleteById(characterInfoId);

		verify(characterInfoRepository).findById(characterInfoId);
		verify(mapper).mapToDto(any(CharacterInfo.class), eq(CharacterInfoDto.class));
		verify(characterInfoRepository).deleteById(characterInfoId);
	}

	@Test
	void testDeleteByIdWhenCharacterInfoNotExist() {
		final CharacterInfoId characterInfoId = createCharacterInfoId();

		when(characterInfoRepository.findById(characterInfoId)).thenThrow(CharacterInfoNotFoundException.class);

		assertThrows(CharacterInfoNotFoundException.class, () -> characterInfoService.deleteById(characterInfoId));
		verify(characterInfoRepository, never()).deleteById(characterInfoId);
	}

	private CharacterInfo createCharacterInfo(CharacterInfoId characterInfoId) {
		CharacterInfo characterInfo = new CharacterInfo();
		characterInfo.setId(characterInfoId);
		characterInfo.setCharacterName("Dom Cobb");

		return characterInfo;
	}

	private CharacterInfoDto createCharacterInfoDto(CharacterInfoId characterInfoId) {
		CharacterInfoDto characterInfoDto = new CharacterInfoDto();
		characterInfoDto.setId(characterInfoId);
		characterInfoDto.setCharacterName("Dom Cobb");

		return characterInfoDto;
	}

	private CharacterInfoId createCharacterInfoId() {
		CharacterInfoId characterInfoId = new CharacterInfoId();
		characterInfoId.setActorId(1L);
		characterInfoId.setMovieId(1L);

		return characterInfoId;
	}

	private ActorDto createActorDto() {
		ActorDto actorDto = new ActorDto();
		actorDto.setName("Actor 1");
		actorDto.setBirthDate("11-11-1984");
		actorDto.setNationality("American");
		actorDto.setAwards("Oscar");

		return actorDto;
	}
}
