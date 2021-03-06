package com.jjss.civideo.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.jjss.civideo.global.exception.dto.ErrorResponseDto;
import com.jjss.civideo.global.util.JwtProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = getToken(request);
		if (request.getRequestURI().contains("/auth")) {
			filterChain.doFilter(request, response);
		} else {
			try {
				Authentication authentication = JwtProvider.authenticate(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				handleUnauthorized(response, e);
			}
		}

	}

	private String getToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
			return null;
		}

		return authorizationHeader.substring(7);
	}

	private void handleUnauthorized(HttpServletResponse response, Exception e) throws IOException {
		log.info("JWT Token authenticate exception in " + this.getClass().getSimpleName(), e);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().write(new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).writeValueAsString(new ErrorResponseDto(e)));
	}

}

