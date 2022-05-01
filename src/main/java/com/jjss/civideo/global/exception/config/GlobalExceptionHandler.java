package com.jjss.civideo.global.exception.config;

import com.jjss.civideo.domain.auth.exception.OAuth2AuthorizationRequestMissingException;
import com.jjss.civideo.domain.couple.exception.SameUserException;
import com.jjss.civideo.global.exception.ConflictDataException;
import com.jjss.civideo.global.exception.ForbiddenException;
import com.jjss.civideo.global.exception.NotFoundDataException;
import com.jjss.civideo.global.exception.dto.ErrorResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
@EnableAspectJAutoProxy
public class GlobalExceptionHandler {

	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponseDto handle400(BindException e) {
		String message = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		return new ErrorResponseDto(message);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponseDto handle400(HttpMessageNotReadableException e) {
		return new ErrorResponseDto(e);
	}

	@ExceptionHandler({
		NotFoundDataException.class,
		SameUserException.class
	})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponseDto handle400(Exception e) {
		String message = e.getMessage();
		return new ErrorResponseDto(message);
	}

	@ExceptionHandler({
		AuthenticationException.class,
		OAuth2AuthorizationRequestMissingException.class,
		ExpiredJwtException.class,
		MalformedJwtException.class
	})
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponseDto handle401(Exception e) {
		return new ErrorResponseDto(e);
	}

	@ExceptionHandler(ForbiddenException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponseDto handle403(ForbiddenException e) {
		String message = e.getMessage();
		return new ErrorResponseDto(message);
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponseDto handle404(NoHandlerFoundException e) {
		String message = e.getMessage();
		return new ErrorResponseDto(message);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ErrorResponseDto handle405(HttpRequestMethodNotSupportedException e) {
		String message = e.getMessage();
		return new ErrorResponseDto(message);
	}

	@ExceptionHandler(ConflictDataException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponseDto handle409(ConflictDataException e) {
		String message = e.getMessage();
		return new ErrorResponseDto(message);
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public ErrorResponseDto handle415(HttpMediaTypeNotSupportedException e) {
		String message = e.getMessage();
		return new ErrorResponseDto(message);
	}

}
