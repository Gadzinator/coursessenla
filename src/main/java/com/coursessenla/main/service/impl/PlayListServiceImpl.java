package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.exception.PlayListNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.PlayListRepositoryImpl;
import com.coursessenla.main.service.PlayListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PlayListServiceImpl implements PlayListService {

	private final PlayListRepositoryImpl playListRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public void save(PlayListDto playListDto) {
		log.info("Starting method save: {}", playListDto);
		playListRepository.save(mapper.mapToEntity(playListDto, Playlist.class));
		log.info("Ending method save: {}", playListDto);
	}

	@Override
	public PlayListDto findById(long id) {
		log.info("Starting method findById: {}", id);
		final PlayListDto playListDto = playListRepository.findById(id)
				.map(playlist -> mapper.mapToDto(playlist, PlayListDto.class))
				.orElseThrow(() -> new PlayListNotFoundException(String.format("Playlist with id %d was not found", id)));
		log.info("Ending method findById: {}", playListDto);

		return playListDto;
	}

	@Override
	public Page<PlayListDto> findAll(Pageable pageable) {
		log.info("Starting method findAll: {}", pageable);
		final Page<PlayListDto> playListDtoPage = playListRepository.findAll(pageable)
				.map(playlist -> mapper.mapToDto(playlist, PlayListDto.class));

		if (playListDtoPage.isEmpty()) {
			log.warn("No playlists were found, throwing PlayListNotFoundException");
			throw new PlayListNotFoundException("No playlists were found");
		}

		log.info("Ending method findAll: {}", playListDtoPage);

		return playListDtoPage;
	}

	@Transactional
	@Override
	public void update(PlayListDto playListDtoUpdate) {
		log.info("Starting method update: {}", playListDtoUpdate);
		findById(playListDtoUpdate.getId());
		playListRepository.update(mapper.mapToEntity(playListDtoUpdate, Playlist.class));
		log.info("Ending method update: {}", playListDtoUpdate);
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting method deleteById: {}", id);
		findById(id);
		playListRepository.deleteById(id);
		log.info("Ending method deleteById: {}", id);
	}
}
