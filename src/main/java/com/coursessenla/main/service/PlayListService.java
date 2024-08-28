package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.PlayListDto;

public interface PlayListService {
	void save(PlayListDto playListDto);

	PlayListDto findById(long id);

	void updateById(long id, PlayListDto playListDtoUpdate);

	void deleteById(long id);
}
