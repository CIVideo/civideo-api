package com.jjss.civideo.global.exception.dto;


import com.jjss.civideo.global.exception.ConflictDataException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConflictResponseDto {

    private final String field;
    private final Object value;
    private final String message;

    public static ConflictResponseDto of(ConflictDataException exception) {
        return new ConflictResponseDto(exception.getField(), exception.getValue(), exception.getMessage());
    }

}
