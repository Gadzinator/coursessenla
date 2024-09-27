package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.ActorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActorService {
	void save(ActorDto actorDto);

	ActorDto findById(long id);

	ActorDto findByName(String name);

	Page<ActorDto> findAll(Pageable pageable);

	void update(ActorDto actorDto);

	void deleteById(long id);
}
