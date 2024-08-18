package com.coursessenla.main;

import com.coursessenla.di.ApplicationContext;
import com.coursessenla.di.ApplicationRunner;
import com.coursessenla.main.controller.Controller;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = ApplicationRunner.run("com.coursessenla");
		context.getObject(Controller.class).executeController();
	}
}
