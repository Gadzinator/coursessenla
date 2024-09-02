package com.coursessenla.main.config;

import com.coursessenla.main.aop.TransactionAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.sql.DataSource;

@Configuration
@EnableAspectJAutoProxy
public class TransactionAspectConfig {

	@Bean
	public TransactionAspect transactionAspect(ConnectionHolder connectionHolder) {
		return new TransactionAspect(connectionHolder);
	}

	@Bean
	public ConnectionHolder connectionHolder(DataSource dataSource) {
		return new ConnectionHolder(dataSource);
	}
}
