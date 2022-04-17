package com.jjss.civideo.global.config.auth;

import com.jjss.civideo.global.util.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if (token != null) {
            try {
                Authentication authentication = JwtProvider.authenticate(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (SignatureException | ExpiredJwtException | MalformedJwtException | IllegalArgumentException e) {
                logger.info("JWT Token authenticate exception: {}", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String AuthorizationHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(AuthorizationHeader) || !AuthorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return AuthorizationHeader.substring(7);
    }

}

