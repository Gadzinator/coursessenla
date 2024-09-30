package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.DirectorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DirectorService {
	void save(DirectorDto directorDto);

	DirectorDto findById(long id);

	DirectorDto findByName(String name);

	Page<DirectorDto> findAll(Pageable pageable);

	void update(DirectorDto directorDtoUpdate);

	void deleteById(long id);
}
