package com.jjss.civideo.global.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.NoHandlerFoundException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NotFoundResponseDto {

    private final String requestURL;
    private final String method;
    private final String message;

    public static NotFoundResponseDto of(NoHandlerFoundException exception) {
        return new NotFoundResponseDto(exception.getRequestURL(), exception.getHttpMethod(), exception.getMessage());
    }

}
