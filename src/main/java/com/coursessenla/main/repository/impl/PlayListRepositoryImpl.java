package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.repository.PlayListRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		for (int i = 0; i < playlists.size(); i++) {
			final Playlist playlist = playlists.get(i);
			if (playlist.getId() == id) {
				playlists.set(i, playListUpdate);
			}
		}
	}

	@Override
	public void deleteById(long id) {
		playlists.removeIf(playlist -> playlist.getId() == id);
	}
}
