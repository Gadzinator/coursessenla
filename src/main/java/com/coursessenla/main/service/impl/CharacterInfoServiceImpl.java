package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.domain.entity.CharacterInfo;
import com.coursessenla.main.domain.entity.CharacterInfoId;
import com.coursessenla.main.exception.CharacterInfoNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.CharacterInfoRepositoryImpl;
import com.coursessenla.main.service.ActorService;
import com.coursessenla.main.service.CharacterInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class CharacterInfoServiceImpl implements CharacterInfoService {

	private final CharacterInfoRepositoryImpl characterInfoRepository;
	private final GenericMapper mapper;
	private final ActorService actorService;

	@Transactional
	@Override
	public void save(CharacterInfoDto characterInfoDto) {
		log.info("Starting method save: {}", characterInfoDto);

		final CharacterInfo characterInfo = mapper.mapToEntity(characterInfoDto, CharacterInfo.class);
		final Long actorId = characterInfo.getActor().getId();
		final ActorDto actorDto = actorService.findById(actorId);
		characterInfo.setActor(mapper.mapToEntity(actorDto, Actor.class));

		CharacterInfo existingCharacterInfo = characterInfoRepository.findById(characterInfo.getId()).orElse(null);
		characterInfoRepository.save(Objects.requireNonNullElse(existingCharacterInfo, characterInfo));

		log.info("Ending method save: {}", characterInfoDto);
	}

	@Override
	public CharacterInfoDto findById(CharacterInfoId id) {
		log.info("Starting method findById: {}", id);
		final CharacterInfoDto characterInfoDto = characterInfoRepository.findById(id)
				.map(characterInfo -> mapper.mapToDto(characterInfo, CharacterInfoDto.class))
				.orElseThrow(() -> new CharacterInfoNotFoundException(id));
		log.info("Ending method findById: {}", characterInfoDto);

		return characterInfoDto;
	}

	@Override
	public Page<CharacterInfoDto> findAll(Pageable pageable) {
		log.info("Starting method findAll: {}", pageable);
		final Page<CharacterInfoDto> characterInfoDtoList = characterInfoRepository.findAll(pageable)
				.map(characterInfo -> mapper.mapToDto(characterInfo, CharacterInfoDto.class));

		if (characterInfoDtoList.isEmpty()) {
			log.warn("No characterInfo were found, throwing CharacterInfoNotFoundException");
			throw new CharacterInfoNotFoundException();
		}

		log.info("Ending method findAll: {}", characterInfoDtoList);

		return characterInfoDtoList;
	}

	@Transactional
	@Override
	public void update(CharacterInfoDto characterInfoDto) {
		log.info("Starting method update: {}", characterInfoDto);
		findById(characterInfoDto.getId());
		characterInfoRepository.update(mapper.mapToEntity(characterInfoDto, CharacterInfo.class));
		log.info("Ending method update: {}", characterInfoDto);
	}

	@Transactional
	@Override
	public void deleteById(CharacterInfoId id) {
		log.info("Starting method deleteById: {}", id);
		findById(id);
		characterInfoRepository.deleteById(id);
		log.info("Ending method deleteById: {}", id);
	}
}
