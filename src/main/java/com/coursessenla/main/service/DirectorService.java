package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.DirectorDto;

public interface DirectorService {
	void save(DirectorDto directorDto);

	DirectorDto findById(long id);

	DirectorDto findByName(String name);

	void updateById(long id, DirectorDto directorDtoUpdate);

	void deleteById(long id);
}
