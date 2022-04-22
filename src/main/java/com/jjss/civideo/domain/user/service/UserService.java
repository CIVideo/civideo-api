package com.jjss.civideo.domain.user.service;

import com.jjss.civideo.domain.user.dto.TokenResponseDto;
import com.jjss.civideo.domain.user.entity.Provider;
import com.jjss.civideo.domain.user.entity.User;
import com.jjss.civideo.domain.user.repository.UserRepository;
import com.jjss.civideo.global.util.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenResponseDto createToken(String provider, String token) {
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

            String accessToken = JwtProvider.createAccessToken(user.getId(), user.getProviderId());
            String refreshToken = JwtProvider.createRefreshToken(user.getId(), user.getProviderId());

            redisTemplate.opsForValue().set(refreshToken, user.getId().toString());
            redisTemplate.expire(refreshToken, 60, TimeUnit.DAYS);

            return TokenResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .code(user.getCode())
                    .build();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public TokenResponseDto refresh(String refreshToken) {
        Jws<Claims> claims = JwtProvider.getClaims(refreshToken);
        Claims body = claims.getBody();

        String savedRefreshToken = redisTemplate.opsForValue().get(refreshToken);
        if (savedRefreshToken == null) {
            throw new ExpiredJwtException(claims.getHeader(), body, null);
        }

        long userId = ((Integer) body.get("userId")).longValue();
        String providerId = body.getSubject();

        String newRefreshToken = JwtProvider.createRefreshToken(userId, providerId);
        redisTemplate.delete(refreshToken);
        redisTemplate.opsForValue().set(newRefreshToken, Long.toString(userId));
        redisTemplate.expire(newRefreshToken, 60, TimeUnit.DAYS);

        return TokenResponseDto.builder()
                .accessToken(JwtProvider.createAccessToken(userId, providerId))
                .refreshToken(newRefreshToken)
                .build();
    }

}
