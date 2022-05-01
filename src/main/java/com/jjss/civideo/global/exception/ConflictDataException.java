package com.jjss.civideo.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConflictDataException extends Exception {

	private final String message;

}
