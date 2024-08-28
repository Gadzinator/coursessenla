package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.PlayListRepository;
import com.coursessenla.main.service.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class PlayListServiceImpl implements PlayListService {

	private final PlayListRepository playListRepository;
	private final GenericMapper mapper;

	@Override
	public void save(PlayListDto playListDto) {
		playListRepository.save(mapper.mapToDto(playListDto, Playlist.class));
	}

	@Override
	public PlayListDto findById(long id) {
		return playListRepository.findById(id)
				.map(playlist -> mapper.mapToEntity(playlist, PlayListDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Playlist with id %d was not found", id)));
	}

	@Override
	public void updateById(long id, PlayListDto playListDtoUpdate) {
		findById(id);
		playListRepository.updateById(id, mapper.mapToDto(playListDtoUpdate, Playlist.class));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		playListRepository.deleteById(id);
	}
}
