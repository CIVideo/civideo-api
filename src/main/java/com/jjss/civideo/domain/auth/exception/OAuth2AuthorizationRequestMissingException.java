package com.jjss.civideo.domain.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2AuthorizationRequestMissingException extends RuntimeException {

    private final String requestURL;

}
