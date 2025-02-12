package com.coursessenla.main.security;

import com.coursessenla.main.service.AuthService;
import com.coursessenla.main.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtTokenUtils jwtTokenUtils;
	private final AuthService authService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws
			ServletException,
			IOException {
		String authHeader = request.getHeader("Authorization");
		String userName = null;
		String jwt = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7);
			try {
				userName = jwtTokenUtils.getUserName(jwt);
			} catch (ExpiredJwtException e) {
				logger.debug("Token lifetime has expired");
			}
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				authService.loadUserByUsername(userName);
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, null,
						jwtTokenUtils.getRoles(jwt).stream()
								.map(SimpleGrantedAuthority::new)
								.collect(Collectors.toList()));
				SecurityContextHolder.getContext().setAuthentication(token);
			} catch (UsernameNotFoundException e) {
				log.warn("UsernameNotFoundException: " + e.getMessage());
			}
		}
		filterChain.doFilter(request, response);
	}
}
