package com.coursessenla.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ActorDto {

	private Long id;

	@NotNull(message = "Name cannot be null")
	@Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters long")
	private String name;

	@JsonFormat(pattern = "dd-MM-yyyy")
	@NotNull(message = "BirthDate cannot be null")
	private String birthDate;

	@NotNull(message = "Nationality cannot be null")
	@Size(min = 1, max = 50, message = "Nationality must be between 1 and 50 characters long")
	private String nationality;

	@Size(max = 255, message = "Awards must not exceed 255 characters")
	private String awards;

	private List<CharacterInfoDto> characterInfos;
}
