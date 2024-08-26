package com.coursessenla.main.mapper;

import com.coursessenla.main.domain.dto.ActorDto;
import com.coursessenla.main.domain.entity.Actor;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ActorMapper {

	private final ModelMapper mapper;

	public ActorDto toDto(Actor actor) {
		return mapper.map(actor, ActorDto.class);
	}

	public Actor toEntity(ActorDto actorDto) {
		return mapper.map(actorDto, Actor.class);
	}
}
