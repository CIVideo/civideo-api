package com.jsss.civideo.domain.auth;

import com.jsss.civideo.global.util.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jsss.civideo.domain.auth.OAuth2AuthorizationRequestRepository.MAIN_URL;
import static com.jsss.civideo.domain.auth.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUriAfterLogin = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(MAIN_URL);
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        // TODO: access token 추가 작업
        getRedirectStrategy().sendRedirect(request, response, redirectUriAfterLogin);
    }

}
