package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.mapper.GenericMapperImpl;
import com.coursessenla.main.repository.GenreRepository;
import com.coursessenla.main.service.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

	private final GenreRepository genreRepository;
	private final GenericMapperImpl<Genre, GenreDto> mapper;

	@Override
	public void save(GenreDto genreDto) {
		genreRepository.save(mapper.toEntity(genreDto));
	}

	@Override
	public GenreDto findById(long id) {
		return genreRepository.findById(id)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Genre with id %d was not found", id)));
	}

	@Override
	public GenreDto findByName(String name) {
		return genreRepository.findByName(name)
				.map(mapper::toDto)
				.orElseThrow(() -> new NoSuchElementException(String.format("Genre with name %s was not found", name)));
	}

	@Override
	public void updateById(long id, GenreDto genreDtoUpdate) {
		findById(id);
		genreRepository.updateById(id, mapper.toEntity(genreDtoUpdate));
	}

	@Override
	public List<GenreDto> findAllByNames(List<String> genreNames) {
		List<GenreDto> genreDtoList = new ArrayList<>();

		Map<String, Genre> existingGenres = genreRepository.findAll().stream()
				.collect(Collectors.toMap(Genre::getName, genre -> genre));

		for (String genreName : genreNames) {
			if (existingGenres.containsKey(genreName)) {
				genreDtoList.add(mapper.toDto(existingGenres.get(genreName)));
			} else {
				Genre newGenre = new Genre();
				newGenre.setName(genreName);
				newGenre.setId(findLastId() + 1);
				genreRepository.save(newGenre);
				genreDtoList.add(mapper.toDto(newGenre));
			}
		}

		return genreDtoList;
	}

	@Override
	public void deleteById(long id) {
		findById(id);
		genreRepository.deleteById(id);
	}

	private long findLastId() {
		final List<Genre> genreList = genreRepository.findAll();

		return genreList.stream()
				.map(Genre::getId)
				.max(Comparator.naturalOrder())
				.orElseThrow(() -> new RuntimeException("No genres found"));
	}
}
