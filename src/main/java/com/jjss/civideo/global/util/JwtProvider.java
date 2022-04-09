package com.jjss.civideo.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public final class JwtProvider {

    public static final String ACCESS_TOKEN_NAME = "access_token";
    public static final String REFRESH_TOKEN_NAME = "refresh_token";
    public static final int ACCESS_TOKEN_EXPIRATION_SECONDS = 60 * 30; // 30m (unit: second)
    public static final int REFRESH_TOKEN_EXPIRATION_SECONDS = 60 * 60 * 24 * 7; // 7d (unit: second)

    private static Key KEY;

    private JwtProvider() {
    }

    @Value("${jwt.key}")
    private void setKey(String key) {
        KEY = new SecretKeySpec(
                key.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
    }

    public static String createAccessToken(Long userId, String providerId) {
        return createToken(userId, providerId, ACCESS_TOKEN_EXPIRATION_SECONDS);
    }

    public static String createRefreshToken(Long userId, String email) {
        return createToken(userId, email, REFRESH_TOKEN_EXPIRATION_SECONDS);
    }

    private static String createToken(Long userId, String providerId, int expirationTime) {
        return Jwts.builder()
                .setHeader(createHeaders())
                .setClaims(createPayloads(userId, providerId, expirationTime))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    private static Map<String, Object> createHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        return headers;
    }

    private static Map<String, Object> createPayloads(Long userId, String providerId, int expirationTime) {
        LocalDateTime now = LocalDateTime.now();
        long time = Timestamp.valueOf(now).getTime();
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("iss", "https://api.civideo.com");
        payloads.put("sub", providerId);
        payloads.put("aud", "https://civideo.com");
        payloads.put("iat", time);
        payloads.put("exp", time + expirationTime);
        payloads.put("userId", userId);

        return payloads;
    }

    public static Jws<Claims> getClaims(String jwt) throws SignatureException, ExpiredJwtException, MalformedJwtException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(jwt);
    }

    public static Authentication authenticate(String jwtToken) throws SignatureException, ExpiredJwtException, MalformedJwtException, IllegalArgumentException {
        Jws<Claims> claims = getClaims(jwtToken);
        Claims body = claims.getBody();
        String id = (String) body.get("sub");

        return new UsernamePasswordAuthenticationToken(id, null);
    }

}

