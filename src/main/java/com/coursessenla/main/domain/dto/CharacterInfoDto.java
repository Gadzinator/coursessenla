package com.coursessenla.main.domain.dto;

import com.coursessenla.main.domain.entity.CharacterInfoId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CharacterInfoDto {

	private CharacterInfoId id;

	@NotNull(message = "Movie id cannot be null")
	private Long movieId;

	@NotNull(message = "Actor id cannot be null")
	private Long actorId;

	@NotNull(message = "Character Name cannot be null")
	@Size(min = 1, max = 255, message = "Character name must be between 1 and 255 characters long")
	private String characterName;
}
