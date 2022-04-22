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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public final class JwtProvider {

    public static final String ACCESS_TOKEN_NAME = "access_token";
    public static final String REFRESH_TOKEN_NAME = "refresh_token";
    public static final int ACCESS_TOKEN_EXPIRATION_MILLISECONDS = 1000 * 60 * 30; // 30m (unit: ms)
    public static final int REFRESH_TOKEN_EXPIRATION_MILLISECONDS = 1000 * 60 * 60 * 24 * 60; // 60d (unit: ms)

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
        Date exp = new Date();
        exp.setTime(exp.getTime() + ACCESS_TOKEN_EXPIRATION_MILLISECONDS);
        return createToken(userId, providerId, exp);
    }

    public static String createRefreshToken(Long userId, String providerId) {
        Date exp = new Date();
        exp.setTime(exp.getTime() + REFRESH_TOKEN_EXPIRATION_MILLISECONDS);
        return createToken(userId, providerId, exp);
    }

    public static Authentication authenticate(String jwtToken) throws SignatureException, ExpiredJwtException, MalformedJwtException, IllegalArgumentException {
        Jws<Claims> claims = getClaims(jwtToken);
        Claims body = claims.getBody();

        long userId = ((Integer) body.get("userId")).longValue();

        return new UsernamePasswordAuthenticationToken(userId, "", Set.of(new SimpleGrantedAuthority("USER")));
    }

    public static Jws<Claims> getClaims(String jwtToken) throws ExpiredJwtException, MalformedJwtException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(jwtToken);
    }

    private static String createToken(Long userId, String providerId, Date ext) {
        return Jwts.builder()
                .setHeader(createHeaders())
                .setClaims(createPayloads(userId, providerId))
                .setExpiration(ext)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    private static Map<String, Object> createHeaders() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        return headers;
    }

    private static Map<String, Object> createPayloads(Long userId, String providerId) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("iss", "https://api.civideo.com");
        payloads.put("sub", providerId);
        payloads.put("aud", "https://civideo.com");
        payloads.put("userId", userId);

        return payloads;
    }

}

