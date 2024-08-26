package com.coursessenla.main.mapper;

import com.coursessenla.main.domain.dto.PlayListDto;
import com.coursessenla.main.domain.entity.Playlist;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PlayListMapper {

	private final ModelMapper mapper;

	public PlayListDto toDto(Playlist playlist) {
		return mapper.map(playlist, PlayListDto.class);
	}

	public Playlist toEntity(PlayListDto playListDto) {
		return mapper.map(playListDto, Playlist.class);
	}
}
