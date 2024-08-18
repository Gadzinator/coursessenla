package com.coursessenla.main.controller;

import com.coursessenla.di.annotation.Autowire;
import com.coursessenla.di.annotation.Component;
import com.coursessenla.main.service.Service;

@Component
public class ControllerImpl implements Controller {
	private final Service service;

	@Autowire
	public ControllerImpl(Service service) {
		this.service = service;
	}

	@Override
	public void executeController() {
		System.out.println("Execute method in Controller");
		service.execute();
	}
}
