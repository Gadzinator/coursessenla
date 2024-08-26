package com.coursessenla.main.domain.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserRole {

	private long userId;
	private long roleId;
}
