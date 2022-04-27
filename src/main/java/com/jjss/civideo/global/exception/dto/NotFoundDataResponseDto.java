package com.jjss.civideo.global.exception.dto;

import com.jjss.civideo.global.exception.NotFoundDataException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NotFoundDataResponseDto {

	private final String field;
	private final Object value;
	private final String message;

	public static NotFoundDataResponseDto of(NotFoundDataException exception) {
		return new NotFoundDataResponseDto(exception.getField(), exception.getValue(), exception.getMessage());
	}

}
