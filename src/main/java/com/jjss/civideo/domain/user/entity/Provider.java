package com.jjss.civideo.domain.user.entity;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

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
        public HttpHeaders getHeader(String token) {
            return HttpHeaders.EMPTY;
        }

        @Override
        public String getProviderId(Map<String, Object> body) {
            return body.get("sub").toString();
        }
    },
    KAKAO {
        @Override
        public String getTokenInfoUrl(String token) {
            return "https://kapi.kakao.com/v2/user/me";
        }

        @Override
        public HttpHeaders getHeader(String token) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            return httpHeaders;
        }

        @Override
        public String getProviderId(Map<String, Object> body) {
            return body.get("id").toString();
        }
    };

    public abstract String getTokenInfoUrl(String token);

    public abstract HttpHeaders getHeader(String token);

    public abstract String getProviderId(Map<String, Object> body);
}
