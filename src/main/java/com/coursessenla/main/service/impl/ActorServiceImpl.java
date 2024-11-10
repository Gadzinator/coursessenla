package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.domain.entity.CharacterInfo;
import com.coursessenla.main.exception.ActorNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.ActorRepositoryImpl;
import com.coursessenla.main.service.ActorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ActorServiceImpl implements ActorService {

	private final ActorRepositoryImpl actorRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public void save(ActorDto actorDto) {
		log.info("Starting method save: {}", actorDto);
		actorRepository.save(mapper.mapToEntity(actorDto, Actor.class));
		log.info("Ending method save: {}", actorDto);
	}

	@Transactional
	@Override
	public ActorDto findById(long id) {
		log.info("Starting method findById: {}", id);
		final ActorDto actorDto = actorRepository.findById(id)
				.map(actor -> mapper.mapToDto(actor, ActorDto.class))
				.orElseThrow(() -> new ActorNotFoundException(id));
		log.info("Ending method findById: {}", actorDto);

		return actorDto;
	}

	@Transactional
	@Override
	public ActorDto findByName(String name) {
		log.info("Starting method findByName: {}", name);
		final ActorDto actorDto = actorRepository.findByName(name)
				.map(actor -> mapper.mapToDto(actor, ActorDto.class))
				.orElseThrow(() -> new ActorNotFoundException(name));
		log.info("Ending method findByName: {}", actorDto);

		return actorDto;
	}

	@Transactional
	@Override
	public Page<ActorDto> findAll(Pageable pageable) {
		log.info("Starting method findAll: {}", pageable);
		final Page<ActorDto> actorDtoPage = actorRepository.findAll(pageable)
				.map(actor -> mapper.mapToDto(actor, ActorDto.class));

		if (actorDtoPage.isEmpty()) {
			log.warn("No actors were found, throwing ActorNotFoundException");
			throw new ActorNotFoundException();
		}

		log.info("Ending method findAll. Total elements: {}", actorDtoPage.getTotalElements());

		return actorDtoPage;
	}


	@Transactional
	@Override
	public void update(ActorDto actorDtoUpdate) {
		log.info("Starting method update: {}", actorDtoUpdate);
		final ActorDto existingActorDto = findById(actorDtoUpdate.getId());
		final List<CharacterInfoDto> existingCharacterInfos = existingActorDto.getCharacterInfos();
		List<CharacterInfo> saveCharactersInfo = new ArrayList<>();
		for (CharacterInfoDto existingCharacterInfo : existingCharacterInfos) {
			log.info("Updating CharacterInfo: {}", existingCharacterInfo);
			final CharacterInfo characterInfo = mapper.mapToEntity(existingCharacterInfo, CharacterInfo.class);
			log.info("Mapped CharacterInfo: {}", characterInfo);
			saveCharactersInfo.add(characterInfo);
		}
		final Actor updatedActor = mapper.mapToEntity(actorDtoUpdate, Actor.class);
		updatedActor.setCharacterInfos(saveCharactersInfo);
		actorRepository.update(updatedActor);

		log.info("Ending method update: {}", actorDtoUpdate);
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting method deleteById: {}", id);
		findById(id);
		actorRepository.deleteById(id);
		log.info("Ending method delete");
	}
}
