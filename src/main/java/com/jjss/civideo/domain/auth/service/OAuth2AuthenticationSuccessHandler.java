package com.jjss.civideo.domain.auth.service;

import com.jjss.civideo.domain.auth.entity.User;
import com.jjss.civideo.domain.auth.repository.UserRepository;
import com.jjss.civideo.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.jjss.civideo.domain.auth.service.OAuth2AuthorizationRequestRepository.MAIN_URL;
import static com.jjss.civideo.domain.auth.service.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUriAfterLogin = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(MAIN_URL);
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();

        String email = (String) attributes.get("email");

        User user = userRepository.findByEmail(email).orElseGet(() -> User.builder()
                .email(email)
                .build());

        userRepository.save(user);

        getRedirectStrategy().sendRedirect(request, response, redirectUriAfterLogin);
    }

}
