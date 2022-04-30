package com.jjss.civideo.domain.couple.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SameUserException extends Exception {

	private final String message;

}
