package com.jjss.civideo.domain.auth.entity;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Provider {

    GOOGLE {
        @Override
        public String getTokenInfoUrl(String token) {
            return UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
                    .queryParam("id_token", token)
                    .toUriString();
        }

        @Override
        public String getEmail(Map<String, Object> attributes) {
            return (String) attributes.get("email");
        }

        @Override
        public HttpHeaders getHeader(String token) {
            return HttpHeaders.EMPTY;
        }
    },
    KAKAO {
        @Override
        public String getTokenInfoUrl(String token) {
            return "https://kapi.kakao.com/v2/user/me";
        }

        @Override
        public String getEmail(Map<String, Object> attributes) {
            Object accountInfo = attributes.get("kakao_account");
            if (!(accountInfo instanceof LinkedHashMap)) {
                return null;
            }

            return (String) ((LinkedHashMap<?, ?>) accountInfo).get("email");
        }

        @Override
        public HttpHeaders getHeader(String token) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            return httpHeaders;
        }
    };

    public abstract String getTokenInfoUrl(String token);

    public abstract String getEmail(Map<String, Object> attributes);

    public abstract HttpHeaders getHeader(String token);

}
