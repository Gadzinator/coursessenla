package com.coursessenla.main.repository.impl;

import com.coursessenla.main.domain.entity.CharacterInfo;
import com.coursessenla.main.domain.entity.CharacterInfoId;
import com.coursessenla.main.repository.AbstractDao;
import org.springframework.stereotype.Repository;

@Repository
public class CharacterInfoRepositoryImpl extends AbstractDao<CharacterInfo, CharacterInfoId> {

	public CharacterInfoRepositoryImpl() {
		super(CharacterInfo.class);
	}
}
