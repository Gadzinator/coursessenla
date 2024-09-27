package com.coursessenla.main.mapper;

import org.hibernate.collection.spi.PersistentBag;
import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GenericMapperImpl implements GenericMapper {

	private final ModelMapper mapper;

	public GenericMapperImpl(ModelMapper mapper) {
		this.mapper = mapper;
		configureMapper();
	}

	@Override
	public <T> T mapToDto(Object entity, Class<T> dtoClass) {
		return mapper.map(entity, dtoClass);
	}

	@Override
	public <T> T mapToEntity(Object dto, Class<T> entityClass) {
		return mapper.map(dto, entityClass);
	}

	private void configureMapper() {
		Converter<String, LocalDate> toLocalDate = new Converter<>() {

			@Override
			public LocalDate convert(MappingContext<String, LocalDate> context) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				return LocalDate.parse(context.getSource(), formatter);
			}
		};

		mapper.addConverter(toLocalDate);
	}
}
