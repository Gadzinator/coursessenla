package com.coursessenla.main.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		String message = "Unauthorized";
		String username = request.getRemoteUser();
		if (username != null && !username.isEmpty()) {
			message += ", " + username + " is not authenticated";
			log.warn(message + "url" + request.getRequestURI());
		} else {
			message += ", Anonymous user is not authenticated";
			log.warn(message + ", url" + request.getRequestURI());
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(message);
	}
}
