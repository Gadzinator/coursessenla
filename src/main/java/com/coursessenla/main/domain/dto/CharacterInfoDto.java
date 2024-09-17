package com.coursessenla.main.domain.dto;

import com.coursessenla.main.domain.entity.CharacterInfoId;
import lombok.Data;

@Data
public class CharacterInfoDto {

	private CharacterInfoId id;
	private Long movieId;
	private Long actorId;
	private String characterName;
}
