package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.exception.DirectorNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.DirectorRepositoryImpl;
import com.coursessenla.main.service.DirectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DirectorServiceImpl implements DirectorService {

	private final DirectorRepositoryImpl directorRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public void save(DirectorDto directorDto) {
		log.info("Starting method save: {}", directorDto);
		final Director director = mapper.mapToEntity(directorDto, Director.class);
		directorRepository.save(director);
		log.info("Ending method save: {}", directorDto);
	}

	@Override
	public DirectorDto findById(long id) {
		log.info("Starting method findById: {}", id);
		final DirectorDto directorDto = directorRepository.findById(id)
				.map(director -> mapper.mapToDto(director, DirectorDto.class))
				.orElseThrow(() -> new DirectorNotFoundException(String.format("Director with id %d was not found", id)));
		log.info("Ending method findById: {}", directorDto);

		return directorDto;
	}

	@Override
	public DirectorDto findByName(String name) {
		log.info("Starting method findByName: {}", name);
		final DirectorDto directorDto = directorRepository.findByName(name)
				.map(director -> mapper.mapToDto(director, DirectorDto.class))
				.orElseThrow(() -> new DirectorNotFoundException(String.format("Director with name %s was not found", name)));
		log.info("Ending method findByName: {}", directorDto);

		return directorDto;
	}

	@Override
	public Page<DirectorDto> findAll(Pageable pageable) {
		log.info("Starting method findAll: {}", pageable);
		final Page<DirectorDto> directorDtoPage = directorRepository.findAll(pageable)
				.map(director -> mapper.mapToDto(director, DirectorDto.class));

		if (directorDtoPage.isEmpty()) {
			log.warn("No director were found, throwing DirectorNotFoundException");
			throw new DirectorNotFoundException("No director were found");
		}

		log.info("Ending method findAll: {}", directorDtoPage);

		return directorDtoPage;
	}

	@Transactional
	@Override
	public void update(DirectorDto directorDtoUpdate) {
		log.info("Starting method update: {}", directorDtoUpdate);
		findById(directorDtoUpdate.getId());
		directorRepository.update(mapper.mapToEntity(directorDtoUpdate, Director.class));
		log.info("Ending method update: {}", directorDtoUpdate);
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting method deleteById: {}", id);
		findById(id);
		directorRepository.deleteById(id);
		log.info("Ending method deleteById: {}", id);
	}
}
