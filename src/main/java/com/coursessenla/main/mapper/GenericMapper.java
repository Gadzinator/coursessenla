package com.coursessenla.main.mapper;

public interface GenericMapper {

	<T> T mapToDto(Object entity, Class<T> dtoClass);

	<T> T mapToEntity(Object dto, Class<T> entityClass);
}
