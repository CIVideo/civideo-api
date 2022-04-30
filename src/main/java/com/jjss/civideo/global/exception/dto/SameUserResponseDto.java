package com.jjss.civideo.global.exception.dto;

import com.jjss.civideo.domain.couple.exception.SameUserException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SameUserResponseDto {

	private final String message;

	public static SameUserResponseDto of(SameUserException exception) {
		return new SameUserResponseDto(exception.getMessage());
	}

}
