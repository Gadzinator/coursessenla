package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.exception.GenreNotFoundException;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.GenreRepositoryImpl;
import com.coursessenla.main.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

	private final GenreRepositoryImpl genreRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public void save(GenreDto genreDto) {
		log.info("Starting method save: {}", genreDto);
		genreRepository.save(mapper.mapToEntity(genreDto, Genre.class));
		log.info("Ending method save: {}", genreDto);
	}

	@Transactional
	@Override
	public GenreDto findById(long id) {
		log.info("Starting method findById: {}", id);
		final GenreDto genreDto = genreRepository.findById(id)
				.map(genre -> mapper.mapToDto(genre, GenreDto.class))
				.orElseThrow(() -> new GenreNotFoundException(String.format("Genre with id %d was not found", id)));
		log.info("Ending method findById: {}", genreDto);

		return genreDto;
	}

	@Transactional
	@Override
	public GenreDto findByName(String name) {
		log.info("Starting method findByName: {}", name);
		final GenreDto genreDto = genreRepository.findByName(name)
				.map(genre -> mapper.mapToDto(genre, GenreDto.class))
				.orElseThrow(() -> new GenreNotFoundException(String.format("Genre with name %s was not found", name)));
		log.info("Ending method findByName: {}", genreDto);

		return genreDto;
	}

	@Override
	public Page<GenreDto> findAll(Pageable pageable) {
		log.info("Starting method findAll: {}", pageable);
		final Page<GenreDto> genreDtoPage = genreRepository.findAll(pageable)
				.map(genre -> mapper.mapToDto(genre, GenreDto.class));

		if (genreDtoPage.isEmpty()) {
			log.warn("No genre were found, throwing DirectorNotFoundException");
			throw new GenreNotFoundException("No genre were found");
		}

		log.info("Ending method findAll: {}", genreDtoPage);

		return genreDtoPage;
	}

	@Transactional
	@Override
	public void update(GenreDto genreDtoUpdate) {
		log.info("Starting method update: {}", genreDtoUpdate);
		findById(genreDtoUpdate.getId());
		genreRepository.update(mapper.mapToEntity(genreDtoUpdate, Genre.class));
		log.info("Ending method update: {}", genreDtoUpdate);
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		log.info("Starting method deleteById: {}", id);
		findById(id);
		genreRepository.deleteById(id);
		log.info("Ending method deleteById: {}", id);
	}
}
