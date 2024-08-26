package com.coursessenla.main.mapper;

import com.coursessenla.main.domain.dto.DirectorDto;
import com.coursessenla.main.domain.entity.Director;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class DirectorMapper {

	private final ModelMapper mapper;

	public DirectorDto toDto(Director director) {
		return mapper.map(director, DirectorDto.class);
	}

	public Director toEntity(DirectorDto directorDto) {
		return mapper.map(directorDto, Director.class);
	}
}
