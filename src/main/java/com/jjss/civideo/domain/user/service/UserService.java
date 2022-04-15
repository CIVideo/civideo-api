package com.jjss.civideo.domain.user.service;

import com.jjss.civideo.domain.user.dto.TokenResponseDto;
import com.jjss.civideo.domain.user.entity.Provider;
import com.jjss.civideo.domain.user.entity.User;
import com.jjss.civideo.domain.user.repository.UserRepository;
import com.jjss.civideo.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public TokenResponseDto createAccessToken(String provider, String token) {
        Provider oAuth2Provider = Provider.valueOf(provider.toUpperCase());

        try {
            Map<?, ?> body = oAuth2Provider.getTokenInfo(token);

            if (body == null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            oAuth2Provider.logout(token);

            Map<String, Object> checkedBody = body.entrySet()
                    .stream()
                    .filter((entry) -> entry.getKey() instanceof String)
                    .collect(Collectors.toMap(entry -> (String) entry.getKey(), Map.Entry::getValue));

            String id = oAuth2Provider.getProviderId(checkedBody);
            User user = userRepository.findByProviderId(id).orElseGet(() -> User.builder()
                    .providerId(id)
                    .provider(oAuth2Provider)
                    .build());

            if (user.getId() == null) {
                userRepository.save(user);
            }

            return TokenResponseDto.builder()
                    .accessToken(JwtProvider.createAccessToken(user.getId(), user.getProviderId()))
                    .code(user.getCode())
                    .build();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

}
