package com.coursessenla.main.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException {
		String clientIp = request.getRemoteAddr();
		String timestamp = java.time.LocalDateTime.now().toString();
		String errorMessage = "You're not logged in";

		log.warn("Authentication failed for IP: '{}' at '{}'. Reason: {}",
				clientIp, timestamp, exception.getMessage());

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(errorMessage);
	}
}
