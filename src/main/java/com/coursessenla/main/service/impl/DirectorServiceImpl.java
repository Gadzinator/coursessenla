package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.domain.entity.Director;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.DirectorRepository;
import com.coursessenla.main.service.DirectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class DirectorServiceImpl implements DirectorService {

	private final DirectorRepository directorRepository;
	private final GenericMapper mapper;

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
	public void updateById(long id, DirectorDto directorDtoUpdate) {
		findById(id);
		directorRepository.updateById(id, mapper.mapToDto(directorDtoUpdate, Director.class));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		directorRepository.deleteById(id);
	}
}
