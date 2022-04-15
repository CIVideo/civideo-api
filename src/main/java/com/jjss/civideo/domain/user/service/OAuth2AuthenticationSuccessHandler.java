package com.jjss.civideo.domain.user.service;

import com.jjss.civideo.domain.user.entity.Provider;
import com.jjss.civideo.domain.user.entity.User;
import com.jjss.civideo.domain.user.repository.UserRepository;
import com.jjss.civideo.global.config.auth.CustomOAuth2UserService;
import com.jjss.civideo.global.util.CookieUtil;
import com.jjss.civideo.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jjss.civideo.domain.user.service.OAuth2AuthorizationRequestRepository.MAIN_URL;
import static com.jjss.civideo.domain.user.service.OAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.jjss.civideo.global.util.JwtProvider.ACCESS_TOKEN_EXPIRATION_SECONDS;
import static com.jjss.civideo.global.util.JwtProvider.ACCESS_TOKEN_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String redirectUriAfterLogin = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(MAIN_URL);

        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        Provider oAuth2Provider = Provider.valueOf(provider.toUpperCase());

        String id = authentication.getName();
        User user = userRepository.findByProviderId(id).orElseGet(() -> User.builder()
                .providerId(id)
                .provider(oAuth2Provider)
                .build());

        if (user.getId() == null) {
            userRepository.save(user);
        }

        oAuth2Provider.logout(CustomOAuth2UserService.accessToken.get());

        String accessToken = JwtProvider.createAccessToken(user.getId(), user.getProviderId());
        CookieUtil.addCookie(response, ACCESS_TOKEN_NAME, accessToken, ACCESS_TOKEN_EXPIRATION_SECONDS);

        getRedirectStrategy().sendRedirect(request, response, redirectUriAfterLogin);
    }

}
