package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.PlayListDto;

import java.util.List;

public interface PlayListService {
	void save(PlayListDto playListDto);

	PlayListDto findById(long id);

	List<PlayListDto> findAll();

	void update(PlayListDto playListDtoUpdate);

	void deleteById(long id);
}
