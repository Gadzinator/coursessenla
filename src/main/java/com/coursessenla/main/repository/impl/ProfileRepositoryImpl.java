package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.Profile;
import com.coursessenla.main.repository.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepositoryImpl extends AbstractDao<Profile, Long> {

	protected ProfileRepositoryImpl() {
		super(Profile.class);
	}
}
