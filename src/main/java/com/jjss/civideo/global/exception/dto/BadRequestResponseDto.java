package com.jjss.civideo.global.exception.dto;

import lombok.Getter;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class BadRequestResponseDto {

    private final List<Map<String, String>> errors;

    private BadRequestResponseDto(Errors errors) {
        this.errors = errors.getFieldErrors()
                .stream()
                .map(error -> Map.of("field", error.getField(), "message", Objects.requireNonNull(error.getDefaultMessage())))
                .collect(Collectors.toList());
    }

    public static BadRequestResponseDto of(BindException exception) {
        return new BadRequestResponseDto(exception.getBindingResult());
    }

}
