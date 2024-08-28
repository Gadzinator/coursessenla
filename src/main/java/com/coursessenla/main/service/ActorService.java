package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.ActorDto;

public interface ActorService {
	void save(ActorDto actorDto);

	ActorDto findById(long id);

	ActorDto findByName(String name);

	void update(long id, ActorDto actorDto);

	void deleteById(long id);
}
