package com.coursessenla.main.repository;

import com.coursessenla.main.domain.entity.Playlist;

import java.util.Optional;

public interface PlayListRepository {
	void save(Playlist playlist);

	Optional<Playlist> findById(long id);

	void updateById(long id, Playlist playListUpdate);

	void deleteById(long id);
}
