package com.coursessenla.main.controller.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@WebAppConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = "com.coursessenla")
public class WebMvcConfig implements WebMvcConfigurer {
}
