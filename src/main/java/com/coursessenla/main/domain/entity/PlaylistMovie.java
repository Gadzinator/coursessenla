package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PlaylistMovie {

	private Long playlistId;
	private Long movieId;
}
