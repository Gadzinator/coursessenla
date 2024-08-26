package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Genre {

	private long id;
	private String name;
}
