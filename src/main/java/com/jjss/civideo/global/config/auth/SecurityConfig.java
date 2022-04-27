package com.jjss.civideo.global.config.auth;

import com.jjss.civideo.domain.auth.service.OAuth2AuthenticationFailureHandler;
import com.jjss.civideo.domain.auth.service.OAuth2AuthenticationSuccessHandler;
import com.jjss.civideo.domain.auth.service.OAuth2AuthorizationRequestRepository;
import com.jjss.civideo.global.config.log.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final String[] ignoringMatchers = {"/favicon.ico", "/auth/**", "/error/**", "/docs/**"};
	private final String[] permitAllMatchers = {"/oauth2/**"};

	private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	private final OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint;

	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
			.mvcMatchers(ignoringMatchers);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.cors()
				.configurationSource(this.corsConfigurationSource())
				.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.csrf()
				.disable()
			.formLogin()
				.disable()
			.httpBasic()
				.disable()
			.logout()
				.disable()
			.authorizeRequests()
				.mvcMatchers(permitAllMatchers)
					.permitAll()
				.anyRequest()
					.authenticated()
				.and()
			.oauth2Login()
				.authorizationEndpoint()
					.baseUri("/oauth2/authorize")
					.authorizationRequestRepository(oAuth2AuthorizationRequestRepository)
					.and()
				.redirectionEndpoint()
					.baseUri("/oauth2/callback/*")
					.and()
				.userInfoEndpoint()
					.userService(customOAuth2UserService)
					.and()
				.successHandler(oAuth2AuthenticationSuccessHandler)
				.failureHandler(oAuth2AuthenticationFailureHandler)
				.and()
			.exceptionHandling()
				.authenticationEntryPoint(oAuth2AuthenticationEntryPoint)
				.and()
			.addFilterBefore(new LoggingFilter(), OAuth2LoginAuthenticationFilter.class)
			.addFilterAfter(new JwtAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class);
		// @formatter:on
	}

	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();

		corsConfig.addAllowedOrigin("*");
		corsConfig.addAllowedHeader("*");
		corsConfig.addAllowedMethod("*");
		corsConfig.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return source;
	}

}
