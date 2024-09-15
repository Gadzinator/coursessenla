package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.domain.entity.Actor;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.impl.ActorRepositoryImpl;
import com.coursessenla.main.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ActorServiceImpl implements ActorService {

	private final ActorRepositoryImpl actorRepository;
	private final GenericMapper mapper;

	@Transactional
	@Override
	public void save(ActorDto actorDto) {
		actorRepository.save(mapper.mapToEntity(actorDto, Actor.class));
	}

	@Override
	public ActorDto findById(long id) {
		return actorRepository.findById(id)
				.map(actor -> mapper.mapToEntity(actor, ActorDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Actor with id %d was not found", id)));
	}

	@Override
	public ActorDto findByName(String name) {
		return actorRepository.findByName(name)
				.map(actor -> mapper.mapToEntity(actor, ActorDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Actor with name %s was not found", name)));
	}

	@Override
	public List<ActorDto> findAll() {
		return actorRepository.findAll().stream()
				.map(actor -> mapper.mapToDto(actor, ActorDto.class))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void update(ActorDto actorDtoUpdate) {
		findById(actorDtoUpdate.getId());
		actorRepository.update(mapper.mapToDto(actorDtoUpdate, Actor.class));
	}

	@Transactional
	@Override
	public void deleteById(long id) {
		findById(id);
		actorRepository.deleteById(id);
	}
}
