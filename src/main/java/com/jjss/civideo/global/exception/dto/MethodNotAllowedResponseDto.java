package com.jjss.civideo.global.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodNotAllowedResponseDto {

    private final String requestMethod;
    private final List<String> supportedMethods;
    private final String message;

    public static MethodNotAllowedResponseDto of(HttpRequestMethodNotSupportedException exception) {
        return new MethodNotAllowedResponseDto(exception.getMethod(), Arrays.asList(Objects.requireNonNull(exception.getSupportedMethods())), exception.getMessage());
    }

}
