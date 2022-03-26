package com.jjss.civideo.global.exception.dto;

import lombok.Getter;
import org.springframework.web.servlet.NoHandlerFoundException;

@Getter
public class NotFoundResponseDto {

    private final String requestUrl;
    private final String method;
    private final String message;

    private NotFoundResponseDto(String requestUrl, String method, String message) {
        this.requestUrl = requestUrl;
        this.method = method;
        this.message = message;
    }

    public static NotFoundResponseDto of(NoHandlerFoundException exception) {
        return new NotFoundResponseDto(exception.getRequestURL(), exception.getHttpMethod(), exception.getMessage());
    }

}
