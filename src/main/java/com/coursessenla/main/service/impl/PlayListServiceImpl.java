package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.mapper.PlayListMapper;
import com.coursessenla.main.repository.PlayListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class PlayListServiceImpl implements com.coursessenla.main.service.PlayListService {

	private final PlayListRepository playListRepository;
	private final PlayListMapper mapper;

	@Override
	public void save(PlayListDto playListDto) {
		playListRepository.save(mapper.toEntity(playListDto));
	}

	@Override
	public PlayListDto findById(long id) {
		return playListRepository.findById(id)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Playlist with id %d was not found", id)));
	}

	@Override
	public void updateById(long id, PlayListDto playListDtoUpdate) {
		findById(id);
		playListRepository.updateById(id, mapper.toEntity(playListDtoUpdate));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		playListRepository.deleteById(id);
	}
}
