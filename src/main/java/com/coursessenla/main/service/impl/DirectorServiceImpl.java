package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.DirectorRepositoryImpl;
import com.coursessenla.main.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DirectorServiceImpl implements DirectorService {

	private final DirectorRepositoryImpl directorRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public void save(DirectorDto directorDto) {
		directorRepository.save(mapper.mapToDto(directorDto, Director.class));
	}

	@Override
	public DirectorDto findById(long id) {
		return directorRepository.findById(id)
				.map(director -> mapper.mapToDto(director, DirectorDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Director with id %d was not found", id)));
	}

	@Override
	public DirectorDto findByName(String name) {
		return directorRepository.findByName(name)
				.map(director -> mapper.mapToEntity(director, DirectorDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Director with name %s was not found", name)));
	}

	@Override
	public List<DirectorDto> findAll() {
		return directorRepository.findAll().stream()
				.map(director -> mapper.mapToDto(director, DirectorDto.class))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void update(DirectorDto directorDtoUpdate) {
		findById(directorDtoUpdate.getId());
		directorRepository.update(mapper.mapToDto(directorDtoUpdate, Director.class));
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		findById(id);
		directorRepository.deleteById(id);
	}
}
