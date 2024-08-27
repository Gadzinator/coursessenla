package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.repository.PlayListRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

@Repository
public class PlayListRepositoryImpl implements PlayListRepository {

	private final List<Playlist> playlists = new ArrayList<>();

	@Override
	public void save(Playlist playlist) {
		playlists.add(playlist);
	}

	@Override
	public Optional<Playlist> findById(long id) {
		return playlists.stream()
				.filter(playlist -> playlist.getId() == id)
				.findFirst();
	}

	@Override
	public void updateById(long id, Playlist playListUpdate) {
		final OptionalInt indexOptional = IntStream.range(0, playlists.size())
				.filter(i -> playlists.get(i).getId() == id)
				.findFirst();

		indexOptional.ifPresent(index -> playlists.set(index, playListUpdate));
	}

	@Override
	public void deleteById(long id) {
		playlists.removeIf(playlist -> playlist.getId() == id);
	}
}
