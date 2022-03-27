package com.jjss.civideo.global.exception.dto;

import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.util.List;

@Getter
public class UnsupportedMediaTypeResponseDto {

    private final List<MediaType> supportedMediaTypes;
    private final String message;

    private UnsupportedMediaTypeResponseDto(List<MediaType> supportedMediaTypes, String message) {
        this.supportedMediaTypes = supportedMediaTypes;
        this.message = message;
    }

    public static UnsupportedMediaTypeResponseDto of(HttpMediaTypeNotSupportedException exception) {
        return new UnsupportedMediaTypeResponseDto(exception.getSupportedMediaTypes(), exception.getMessage());
    }

}
