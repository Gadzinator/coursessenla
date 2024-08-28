package com.coursessenla.main.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {

	private long id;
	private String email;
}
