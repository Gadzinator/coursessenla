package com.coursessenla.main.service.impl;

import com.coursessenla.main.domain.dto.GenreDto;
import com.coursessenla.main.domain.entity.Genre;
import com.coursessenla.main.mapper.GenericMapper;
import com.coursessenla.main.repository.GenreRepository;
import com.coursessenla.main.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

	private final GenreRepository genreRepository;
	private final GenericMapper mapper;

	@Override
	public void save(GenreDto genreDto) {
		genreRepository.save(mapper.mapToDto(genreDto, Genre.class));
	}

	@Override
	public GenreDto findById(long id) {
		return genreRepository.findById(id)
				.map(genre -> mapper.mapToEntity(genre, GenreDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Genre with id %d was not found", id)));
	}

	@Override
	public GenreDto findByName(String name) {
		return genreRepository.findByName(name)
				.map(genre -> mapper.mapToDto(genre, GenreDto.class))
				.orElseThrow(() -> new NoSuchElementException(String.format("Genre with name %s was not found", name)));
	}

	@Override
	public void updateById(long id, GenreDto genreDtoUpdate) {
		findById(id);
		genreRepository.updateById(id, mapper.mapToDto(genreDtoUpdate, Genre.class));
	}

	@Override
	public List<GenreDto> findAllByNames(List<String> genreNames) {
		List<GenreDto> genreDtoList = new ArrayList<>();

		Map<String, Genre> existingGenres = genreRepository.findAll().stream()
				.collect(Collectors.toMap(Genre::getName, genre -> genre));

		for (String genreName : genreNames) {
			if (existingGenres.containsKey(genreName)) {
				genreDtoList.add(mapper.mapToDto(existingGenres.get(genreName), GenreDto.class));
			} else {
				Genre newGenre = new Genre();
				newGenre.setName(genreName);
				newGenre.setId(findLastId() + 1);
				genreRepository.save(newGenre);
				genreDtoList.add(mapper.mapToDto(newGenre, GenreDto.class));
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
