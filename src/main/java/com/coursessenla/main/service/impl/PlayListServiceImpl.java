package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.PlayListRepositoryImpl;
import com.coursessenla.main.service.PlayListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlayListServiceImpl implements PlayListService {

	private final PlayListRepositoryImpl playListRepository;
	private final GenericMapper mapper;

	@Transactional
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
	public List<PlayListDto> findAll() {
		return playListRepository.findAll().stream()
				.map(playlist -> mapper.mapToDto(playlist, PlayListDto.class))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void update(PlayListDto playListDtoUpdate) {
		findById(playListDtoUpdate.getId());
		playListRepository.update(mapper.mapToDto(playListDtoUpdate, Playlist.class));
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		findById(id);
		playListRepository.deleteById(id);
	}
}
