package com.coursessenla.main.service;

import com.coursessenla.di.annotation.Autowire;
import com.coursessenla.di.annotation.Component;
import com.coursessenla.main.repository.Repository;

@Component
public class ServiceImpl implements Service {
	private Repository repository;

	@Autowire
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	@Override
	public void execute() {
		System.out.println("Execute method in Service");
		repository.execute();
	}
}
