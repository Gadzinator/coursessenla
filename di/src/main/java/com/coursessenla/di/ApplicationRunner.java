package com.coursessenla.di;

public class ApplicationRunner {

	public static ApplicationContext run(String packageToScan) {
		return new ApplicationContext(packageToScan);
	}
}
