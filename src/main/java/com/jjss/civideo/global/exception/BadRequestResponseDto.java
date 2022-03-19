package com.jjss.civideo.global.exception;

import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BadRequestResponseDto {

    private BadRequestResponseDto() {
    }

    public static List<Map<String, String>> of(Errors errors) {
        return errors.getFieldErrors()
                .stream()
                .map(error -> Map.of("field", error.getField(), "message", Objects.requireNonNull(error.getDefaultMessage())))
                .collect(Collectors.toList());
    }

}
