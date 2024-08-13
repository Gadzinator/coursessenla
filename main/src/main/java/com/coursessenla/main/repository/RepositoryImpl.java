package com.coursessenla.main.repository;

import com.coursessenla.di.annotation.Autowire;
import com.coursessenla.di.annotation.Component;
import com.coursessenla.di.annotation.Value;
import com.coursessenla.main.domain.ValueAnnotationExample;

@Component
public class RepositoryImpl implements Repository {

	@Value("my.param.db")
	public String repositoryValue;
	@Autowire
	private ValueAnnotationExample entity;

	@Override
	public void execute() {
		System.out.println("Execute method in Repository. name - " + repositoryValue);
	}
}
