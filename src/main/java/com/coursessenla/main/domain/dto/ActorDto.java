package com.coursessenla.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ActorDto {

	private long id;
	private String name;
	private String birthDate;
	private String nationality;
	private String awards;
}
