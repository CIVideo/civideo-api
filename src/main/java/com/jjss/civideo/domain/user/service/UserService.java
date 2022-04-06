package com.jjss.civideo.domain.user.service;

import com.jjss.civideo.domain.user.entity.Provider;
import com.jjss.civideo.domain.user.entity.User;
import com.jjss.civideo.domain.user.repository.UserRepository;
import com.jjss.civideo.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String createAccessToken(String provider, String token) {
        Provider oAuth2Provider = Provider.valueOf(provider.toUpperCase());

        try {
            Map<?, ?> body = new RestTemplate()
                    .exchange(oAuth2Provider.getTokenInfoUrl(token), HttpMethod.GET, new HttpEntity<>(oAuth2Provider.getHeader(token)), Map.class)
                    .getBody();

            if (body == null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> checkedBody = body.entrySet()
                    .stream()
                    .filter((entry) -> entry.getKey() instanceof String)
                    .collect(Collectors.toMap(entry -> (String) entry.getKey(), Map.Entry::getValue));

            String email = oAuth2Provider.getEmail(checkedBody);
            User user = userRepository.findByEmail(email).orElseGet(() -> User.builder()
                    .email(email)
                    .provider(oAuth2Provider)
                    .build());

            if (user.getId() == null) {
                userRepository.save(user);
            }

            return JwtProvider.createAccessToken(user.getId(), user.getEmail());
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

}
