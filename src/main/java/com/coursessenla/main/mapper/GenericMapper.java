package com.coursessenla.main.mapper;

public interface GenericMapper<E, D> {
	D toDto(Object entity);

	E toEntity(Object dto);
}
