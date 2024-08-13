package com.coursessenla.main;

import com.coursessenla.di.ApplicationContext;
import com.coursessenla.di.ApplicationRunner;
import com.coursessenla.main.controller.Controller;

import java.lang.reflect.InvocationTargetException;

public class Main {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
		ApplicationContext context = ApplicationRunner.run("com.coursessenla");
		context.getObject(Controller.class).executeController();
	}
}
