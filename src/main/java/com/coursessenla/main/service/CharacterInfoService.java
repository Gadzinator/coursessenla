package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.CharacterInfoId;

import java.util.List;

public interface CharacterInfoService {
	void save(CharacterInfoDto characterInfoDto);

	CharacterInfoDto findById(CharacterInfoId id);

	List<CharacterInfoDto> findAll();

	void update(CharacterInfoDto characterInfoDto);

	void deleteById(CharacterInfoId id);
}
