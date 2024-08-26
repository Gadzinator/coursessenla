package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.mapper.ActorMapper;
import com.coursessenla.main.repository.ActorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class ActorServiceImpl implements com.coursessenla.main.service.ActorService {

	private final ActorRepository actorRepository;
	private final ActorMapper mapper;

	@Override
	public void save(ActorDto actorDto) {
		actorRepository.save(mapper.toEntity(actorDto));
	}

	@Override
	public ActorDto findById(long id) {
		return actorRepository.findById(id)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Actor with id %d was not found", id)));
	}

	@Override
	public ActorDto findByName(String name) {
		return actorRepository.findByName(name)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Actor with name %s was not found", name)));
	}

	@Override
	public void update(long id, ActorDto actorDtoUpdate) {
		findById(id);
		actorRepository.updateById(id, mapper.toEntity(actorDtoUpdate));
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		actorRepository.deleteById(id);
	}
}
