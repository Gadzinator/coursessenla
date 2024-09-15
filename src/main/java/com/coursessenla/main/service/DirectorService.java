package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.DirectorDto;

import java.util.List;

public interface DirectorService {
	void save(DirectorDto directorDto);

	DirectorDto findById(long id);

	DirectorDto findByName(String name);

	List<DirectorDto> findAll();

	void update(DirectorDto directorDtoUpdate);

	void deleteById(long id);
}
