package com.coursessenla.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class Application {

	public static void main(String[] args) throws JsonProcessingException {
		new AnnotationConfigApplicationContext(Application.class);
	}
}
