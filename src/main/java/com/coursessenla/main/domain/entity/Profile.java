package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Profile {

	private long id;
	private String firstName;
	private String lastName;
	private User user;
}
