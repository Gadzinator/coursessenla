package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.PlayListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayListService {
	void save(PlayListDto playListDto);

	PlayListDto findById(long id);

	Page<PlayListDto> findAll(Pageable pageable);

	void update(PlayListDto playListDtoUpdate);

	void deleteById(long id);
}
