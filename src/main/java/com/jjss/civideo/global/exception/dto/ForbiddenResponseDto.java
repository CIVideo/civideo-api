package com.jjss.civideo.global.exception.dto;

import com.jjss.civideo.global.exception.ForbiddenException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ForbiddenResponseDto {

	private final String message;

	public static ForbiddenResponseDto of(ForbiddenException exception) {
		return new ForbiddenResponseDto(exception.getMessage());
	}

}
