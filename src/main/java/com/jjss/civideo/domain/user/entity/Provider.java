package com.jjss.civideo.domain.user.entity;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
    };

    public abstract Map<?, ?> getTokenInfo(String token);

    public abstract String getProviderId(Map<String, Object> body);

    public abstract void logout(String token);

}
