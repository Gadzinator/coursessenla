package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.ActorDto;

import java.util.List;

public interface ActorService {
	void save(ActorDto actorDto);

	ActorDto findById(long id);

	ActorDto findByName(String name);

	List<ActorDto> findAll();

	void update(ActorDto actorDto);

	void deleteById(long id);
}
