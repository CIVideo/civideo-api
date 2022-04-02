package com.jjss.civideo.domain.user.service;

import com.jjss.civideo.domain.user.dto.TokenRequestDto;
import com.jjss.civideo.domain.user.entity.User;
import com.jjss.civideo.domain.user.repository.UserRepository;
import com.jjss.civideo.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public String createAccessToken(TokenRequestDto tokenRequestDto) {
        String token = tokenRequestDto.getToken();

        String tokenInfoUrl = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        switch (tokenRequestDto.getProvider()) {
            case "google":
                tokenInfoUrl = "https://oauth2.googleapis.com/tokeninfo";
                tokenInfoUrl = UriComponentsBuilder.fromHttpUrl(tokenInfoUrl)
                        .queryParam("id_token", token)
                        .toUriString();
                break;
            case "kakao":
                tokenInfoUrl = "https://kapi.kakao.com/v1/user/access_token_info";
                httpHeaders.add("Authorization", "Bearer " + token);
                break;
        }

        try {
            if (tokenInfoUrl == null) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            new RestTemplate().exchange(tokenInfoUrl, HttpMethod.GET, new HttpEntity<>(httpHeaders), HashMap.class);

            String email = tokenRequestDto.getEmail();
            User user = userRepository.findByEmail(email).orElseGet(() -> User.builder()
                    .email(email)
                    .provider(tokenRequestDto.getProvider())
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
