package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@Component
public class Director {

	private long id;
	private String name;
	private LocalDate birthDate;
	private String nationality;
	private String awards;
}
