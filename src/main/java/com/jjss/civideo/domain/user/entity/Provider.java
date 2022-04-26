package com.jjss.civideo.domain.user.entity;

import com.jjss.civideo.domain.user.dto.Key;
import com.jjss.civideo.domain.user.dto.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public enum Provider {

    GOOGLE {
        @Override
        public Map<?, ?> getTokenInfo(String token) {
            String tokenInfoUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
                    .queryParam("id_token", token)
                    .toUriString();

            return new RestTemplate()
                    .exchange(tokenInfoUrl, HttpMethod.GET, new HttpEntity<>(HttpHeaders.EMPTY), Map.class)
                    .getBody();
        }

        @Override
        public String getProviderId(Map<String, Object> body) {
            return body.get("sub").toString();
        }

        @Override
        public void logout(String token) {
            String logoutUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/revoke")
                    .queryParam("token", token)
                    .toUriString();

            new RestTemplate().postForObject(logoutUrl, new HttpEntity<>(HttpHeaders.EMPTY), Void.class);
        }
    },
    KAKAO {
        @Override
        public Map<?, ?> getTokenInfo(String token) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            return new RestTemplate()
                    .exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, new HttpEntity<>(httpHeaders), Map.class)
                    .getBody();
        }

        @Override
        public String getProviderId(Map<String, Object> body) {
            return body.get("id").toString();
        }

        @Override
        public void logout(String token) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(token);

            new RestTemplate().postForObject("https://kapi.kakao.com/v1/user/logout", new HttpEntity<>(httpHeaders), Void.class);
        }
    },
    APPLE {
        @Override
        public Map<?, ?> getTokenInfo(String token) {
            Keys body = new RestTemplate()
                    .exchange("https://appleid.apple.com/auth/keys", HttpMethod.GET, new HttpEntity<>(null), Keys.class)
                    .getBody();

            if (body == null) {
                throw new NullPointerException("Apple Public Key is null!");
            }

            List<Key> keys = body.getKeys();

            for (Key key : keys) {
                try {
                    byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
                    byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

                    BigInteger n = new BigInteger(1, nBytes);
                    BigInteger e = new BigInteger(1, eBytes);

                    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
                    KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
                    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                    return Jwts.parser()
                            .setSigningKey(publicKey)
                            .parseClaimsJws(token)
                            .getBody();
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | SignatureException |
                         MalformedJwtException | ExpiredJwtException ignored) {
                }
            }

            return null;
        }

        @Override
        public String getProviderId(Map<String, Object> body) {
            return body.get("sub").toString();
        }

        @Override
        public void logout(String token) {
        }
    };

    public abstract Map<?, ?> getTokenInfo(String token);

    public abstract String getProviderId(Map<String, Object> body);

    public abstract void logout(String token);

}
