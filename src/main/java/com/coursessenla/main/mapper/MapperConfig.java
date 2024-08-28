package com.coursessenla.main.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public GenericMapper mapper(ModelMapper mapper) {
		return new GenericMapperImpl(mapper);
	}
}
