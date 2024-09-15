package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Playlist;
import com.coursessenla.main.repository.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class PlayListRepositoryImpl extends AbstractDao<Playlist, Long> {

	protected PlayListRepositoryImpl() {
		super(Playlist.class);
	}
}
