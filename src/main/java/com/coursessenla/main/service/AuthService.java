package com.coursessenla.main.service;

import com.coursessenla.main.domain.dto.JwtRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

	String createAuthToken(JwtRequest authRequest);

	UserDetails loadUserByUsername(String userName);
}
