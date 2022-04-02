package com.jjss.civideo.global.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BadRequestResponseDto {

    private final List<Map<String, String>> errors;

    public static BadRequestResponseDto of(BindException exception) {
        return new BadRequestResponseDto(toList(exception.getBindingResult()));
    }

    private static List<Map<String, String>> toList(Errors errors) {
        return errors.getFieldErrors()
                .stream()
                .map(error -> Map.of("field", error.getField(), "message", Objects.requireNonNull(error.getDefaultMessage())))
                .collect(Collectors.toList());
    }

}
