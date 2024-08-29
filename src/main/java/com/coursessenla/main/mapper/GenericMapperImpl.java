package com.coursessenla.main.mapper;

import org.modelmapper.ModelMapper;

public class GenericMapperImpl implements GenericMapper {

	private final ModelMapper mapper;

	public GenericMapperImpl(ModelMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public <T> T mapToDto(Object entity, Class<T> dtoClass) {
		return mapper.map(entity, dtoClass);
	}

	@Override
	public <T> T mapToEntity(Object dto, Class<T> entityClass) {
		return mapper.map(dto, entityClass);
	}
}
