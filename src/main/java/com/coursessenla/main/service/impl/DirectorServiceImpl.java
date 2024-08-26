package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.mapper.DirectorMapper;
import com.coursessenla.main.repository.DirectorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class DirectorServiceImpl implements com.coursessenla.main.service.DirectorService {

	private DirectorRepository directorRepository;
	private DirectorMapper mapper;

	@Override
	public void save(DirectorDto directorDto) {
		directorRepository.save(mapper.toEntity(directorDto));
	}

	@Override
	public DirectorDto findById(long id) {
		return directorRepository.findById(id)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Director with id %d was not found", id)));
	}

	@Override
	public DirectorDto findByName(String name) {
		return directorRepository.findByName(name)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Director with name %s was not found", name)));
	}

	@Override
	public void updateById(long id, DirectorDto directorDtoUpdate) {
		findById(id);
		directorRepository.updateById(id, mapper.toEntity(directorDtoUpdate));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		directorRepository.deleteById(id);
	}
}
