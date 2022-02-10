package com.jsss.civideo.global.config.auth;

import com.jsss.civideo.domain.auth.CustomOAuth2UserService;
import com.jsss.civideo.domain.auth.OAuth2AuthenticationSuccessHandler;
import com.jsss.civideo.domain.auth.OAuth2AuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .cors()
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
                .authorizeRequests()
                    .antMatchers("/oauth2/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                    .and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .authorizationRequestRepository(oAuth2AuthorizationRequestRepository)
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler);
        // @formatter:on
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
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
