package com.jjss.civideo.domain.auth.service;

import com.jjss.civideo.domain.auth.entity.User;
import com.jjss.civideo.domain.auth.repository.UserRepository;
import com.jjss.civideo.global.util.CookieUtil;
import com.jjss.civideo.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.jjss.civideo.domain.auth.service.OAuth2AuthorizationRequestRepository.MAIN_URL;
import static com.jjss.civideo.domain.auth.service.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.jjss.civideo.global.util.JwtProvider.ACCESS_TOKEN_EXPIRATION_SECONDS;
import static com.jjss.civideo.global.util.JwtProvider.ACCESS_TOKEN_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUriAfterLogin = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(MAIN_URL);

        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String email = getOAuth2UserEmail(authentication, provider);

        User user = userRepository.findByEmail(email).orElseGet(() -> User.builder()
                .email(email)
                .provider(provider)
                .build());

        if (user.getId() == null) {
            userRepository.save(user);
        }

        String accessToken = JwtProvider.createAccessToken(user.getId(), email);
        CookieUtil.addCookie(response, ACCESS_TOKEN_NAME, accessToken, ACCESS_TOKEN_EXPIRATION_SECONDS);

        getRedirectStrategy().sendRedirect(request, response, redirectUriAfterLogin);
    }

    private String getOAuth2UserEmail(Authentication authentication, String provider) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();

        String email = null;
        switch (provider) {
            case "google":
                email = (String) attributes.get("email");
                break;
            case "kakao":
                @SuppressWarnings("unchecked")
                LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>) attributes.get("kakao_account");
                email = (String) (accountInfo).get("email");
                break;
        }

        return email;
    }

}
