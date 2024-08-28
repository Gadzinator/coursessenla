package com.coursessenla.main.controller.utils;

import com.coursessenla.main.exception.JsonConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtils {

	private final ObjectMapper objectMapper;

	public JsonUtils(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public String getJson(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new JsonConversionException("Error converting object to JSON", e);
		}
	}
}
