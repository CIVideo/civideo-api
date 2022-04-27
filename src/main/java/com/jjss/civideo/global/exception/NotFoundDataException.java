package com.jjss.civideo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotFoundDataException extends Exception {

	private final String field;
	private final Object value;
	private final String message;

}
