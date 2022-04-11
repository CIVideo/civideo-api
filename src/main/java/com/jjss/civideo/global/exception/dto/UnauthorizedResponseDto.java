package com.jjss.civideo.global.exception.dto;

import com.jjss.civideo.domain.user.exception.OAuth2AuthorizationRequestMissingException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UnauthorizedResponseDto {

    private final String requestUrl;
    private final String message;

    public static UnauthorizedResponseDto of(OAuth2AuthorizationRequestMissingException exception) {
        return new UnauthorizedResponseDto(exception.getRequestURL(), "비정상적인 인증 절차입니다.");
    }

}
