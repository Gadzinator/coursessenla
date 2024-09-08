package com.coursessenla.main.config;

import liquibase.integration.spring.SpringLiquibase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@PropertySource("classpath:/application.properties")
public class LiquibaseConfig {

	private final DataSource dataSource;

	@Value("${changeLogFile}")
	private String changelog;

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog(changelog);

		return liquibase;
	}
}
