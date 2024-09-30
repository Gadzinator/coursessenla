package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.CharacterInfoDto;
import com.coursessenla.main.domain.entity.CharacterInfoId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CharacterInfoService {
	void save(CharacterInfoDto characterInfoDto);

	CharacterInfoDto findById(CharacterInfoId id);

	Page<CharacterInfoDto> findAll(Pageable pageable);

	void update(CharacterInfoDto characterInfoDto);

	void deleteById(CharacterInfoId id);
}
