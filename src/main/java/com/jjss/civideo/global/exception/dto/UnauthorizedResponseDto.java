package com.jjss.civideo.global.exception.dto;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
public class UnauthorizedResponseDto {

    private final String requestUrl;
    private final String message;

    private UnauthorizedResponseDto(String requestUrl, String message) {
        this.requestUrl = requestUrl;
        this.message = message;
    }

    public static UnauthorizedResponseDto of(HttpServletRequest request) {
        return new UnauthorizedResponseDto(request.getRequestURI(), "인증이 필요한 리소스입니다.");
    }

}
