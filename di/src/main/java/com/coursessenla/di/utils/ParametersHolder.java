package com.coursessenla.di.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ParametersHolder {
	private static ParametersHolder INSTANCE;
	private static final String FILE_PATH = "./di/src/main/resources/application.properties";
	private final Properties properties = new Properties();

	private ParametersHolder() {
		readProperties();
	}

	public static ParametersHolder getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ParametersHolder();
		}

		return INSTANCE;
	}

	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}

	private void readProperties() {
		try (FileInputStream fileInputStream = new FileInputStream(FILE_PATH)) {
			properties.load(fileInputStream);
		} catch (IOException e) {
			throw new RuntimeException("Ошибка при чтении файла: " + FILE_PATH, e);
		}
	}
}
