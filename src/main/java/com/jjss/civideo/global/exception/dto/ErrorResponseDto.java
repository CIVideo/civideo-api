package com.jjss.civideo.global.exception.dto;

import com.google.common.base.CaseFormat;
import lombok.Getter;

@Getter
public class ErrorResponseDto {

	private final String message;

	public ErrorResponseDto(Exception exception) {
		this.message = ErrorType.valueOf(
			CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, exception.getClass().getSimpleName())
		).getMessage();
	}

	public ErrorResponseDto(String message) {
		this.message = message;
	}

}
