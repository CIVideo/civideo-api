package com.jjss.civideo.global.exception.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UnsupportedMediaTypeResponseDto {

    private final List<MediaType> supportedMediaTypes;
    private final String message;

    public static UnsupportedMediaTypeResponseDto of(HttpMediaTypeNotSupportedException exception) {
        return new UnsupportedMediaTypeResponseDto(exception.getSupportedMediaTypes(), exception.getMessage());
    }

}
