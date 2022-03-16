package com.jjss.civideo.domain.auth.service;

import com.jjss.civideo.global.util.CookieUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jjss.civideo.domain.auth.service.OAuth2AuthorizationRequestRepository.*;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String redirectUriAfterLogin = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(MAIN_URL);
        redirectUriAfterLogin = UriComponentsBuilder.fromUriString(redirectUriAfterLogin)
                .queryParam("error", exception.getLocalizedMessage())
                .build()
                .toUriString();
        CookieUtil.removeCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtil.removeCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        getRedirectStrategy().sendRedirect(request, response, redirectUriAfterLogin);
    }

}
