package com.jjss.civideo.global.exception.dto;

import com.google.common.base.CaseFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UnauthorizedResponseDto {

    private final String message;

    public static UnauthorizedResponseDto of(Exception exception) {
        return new UnauthorizedResponseDto(
                UnauthorizedType.valueOf(
                        CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, exception.getClass().getSimpleName())
                ).getMessage()
        );
    }

}
