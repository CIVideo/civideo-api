package com.jjss.civideo.domain.user.service;

import com.jjss.civideo.domain.user.dto.TokenRequestDto;
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

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String createAccessToken(TokenRequestDto tokenRequestDto) {
        String token = tokenRequestDto.getToken();
        Provider provider = Provider.valueOf(tokenRequestDto.getProvider().toUpperCase());

        try {
            HashMap<?, ?> body = new RestTemplate()
                    .exchange(provider.getTokenInfoUrl(token), HttpMethod.GET, new HttpEntity<>(provider.getHeader(token)), HashMap.class)
                    .getBody();

            if (body == null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            String email = (String) body.get("email");
            User user = userRepository.findByEmail(email).orElseGet(() -> User.builder()
                    .email(email)
                    .provider(provider)
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
