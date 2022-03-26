package com.jjss.civideo.global.exception.dto;

import lombok.Getter;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class MethodNotAllowedResponseDto {

    private final String requestMethod;
    private final List<String> supportedMethods;
    private final String message;

    private MethodNotAllowedResponseDto(String requestMethod, List<String> supportedMethods, String message) {
        this.requestMethod = requestMethod;
        this.supportedMethods = supportedMethods;
        this.message = message;
    }

    public static MethodNotAllowedResponseDto of(HttpRequestMethodNotSupportedException exception) {
        return new MethodNotAllowedResponseDto(exception.getMethod(), Arrays.asList(Objects.requireNonNull(exception.getSupportedMethods())), exception.getMessage());
    }

}
