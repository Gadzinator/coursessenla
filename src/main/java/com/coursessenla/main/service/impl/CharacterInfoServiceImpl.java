package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.CharacterInfo;
import com.coursessenla.main.domain.entity.CharacterInfoId;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.CharacterInfoRepositoryImpl;
import com.coursessenla.main.service.CharacterInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CharacterInfoServiceImpl implements CharacterInfoService {

	private final CharacterInfoRepositoryImpl characterInfoRepository;
	private final GenericMapper mapper;


	@Transactional
	@Override
	public void save(CharacterInfoDto characterInfoDto) {
		characterInfoRepository.save(mapper.mapToEntity(characterInfoDto, CharacterInfo.class));
	}

	@Override
	public CharacterInfoDto findById(CharacterInfoId id) {
		return characterInfoRepository.findById(id)
				.map(characterInfo -> mapper.mapToDto(characterInfo, CharacterInfoDto.class))
				.orElseThrow(() -> new NoSuchElementException(
						String.format("CharacterInfoService with id %s was not found", id)));
	}

	@Override
	public List<CharacterInfoDto> findAll() {
		return characterInfoRepository.findAll().stream()
				.map(characterInfo -> mapper.mapToDto(characterInfo, CharacterInfoDto.class))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void update(CharacterInfoDto characterInfoDto) {
		findById(characterInfoDto.getId());
		characterInfoRepository.update(mapper.mapToEntity(characterInfoDto, CharacterInfo.class));
	}

	@Transactional
	@Override
	public void deleteById(CharacterInfoId id) {
		findById(id);
		characterInfoRepository.deleteById(id);
	}
}
