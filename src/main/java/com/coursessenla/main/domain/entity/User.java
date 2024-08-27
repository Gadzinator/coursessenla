package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class User {

	private long id;
	private String email;
	private String password;
	private Role role;
}
