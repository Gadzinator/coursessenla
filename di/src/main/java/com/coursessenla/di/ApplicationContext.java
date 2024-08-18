package com.coursessenla.di;

import com.coursessenla.di.config.JavaConfig;
import com.coursessenla.di.injector.BeanInjector;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class ApplicationContext {
	private final BeanInjector beanInjector;
	private final JavaConfig config;

	public ApplicationContext(String packageToScan) {
		Reflections scanner = new Reflections(packageToScan);
		beanInjector = new BeanInjector(scanner);
		config = new JavaConfig(scanner);
	}

	public <T> T getObject(Class<T> type) {
		try {
			Map<Class, Object> beans = beanInjector.getInitializationBeans();
			Class<T> impl = config.resolveImpl(type);

			return beans.containsKey(impl) ? (T) beans.get(impl) : (T) type;
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
			throw new RuntimeException("Ошибка при создании объекта для класса: " + type.getName(), e);
		}
	}
}
