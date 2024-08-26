package com.coursessenla.main.mapper;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.entity.Genre;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GenreMapper {

	private final ModelMapper mapper;

	public GenreDto toDto(Genre genre) {
		return mapper.map(genre, GenreDto.class);
	}

	public Genre toEntity(GenreDto genreDto) {
		return mapper.map(genreDto, Genre.class);
	}
}
