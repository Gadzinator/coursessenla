package com.coursessenla.main.mapper;

import org.modelmapper.ModelMapper;

public class GenericMapperImpl<E, D> implements GenericMapper<E, D> {

	private final ModelMapper mapper;
	private final Class<E> entityClass;
	private final Class<D> dtoClass;

	public GenericMapperImpl(ModelMapper mapper, Class<E> entityClass, Class<D> dtoClass) {
		this.mapper = mapper;
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
	}

	@Override
	public D toDto(Object entity) {
		return mapper.map(entity, dtoClass);
	}

	@Override
	public E toEntity(Object dto) {
		return mapper.map(dto, entityClass);
	}
}
