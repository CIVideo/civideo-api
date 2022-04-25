package com.jjss.civideo.global.exception.config;

import com.jjss.civideo.domain.user.exception.OAuth2AuthorizationRequestMissingException;
import com.jjss.civideo.global.exception.ConflictDataException;
import com.jjss.civideo.global.exception.NotFoundDataException;
import com.jjss.civideo.global.exception.dto.BadRequestResponseDto;
import com.jjss.civideo.global.exception.dto.ConflictResponseDto;
import com.jjss.civideo.global.exception.dto.MethodNotAllowedResponseDto;
import com.jjss.civideo.global.exception.dto.NotFoundDataResponseDto;
import com.jjss.civideo.global.exception.dto.NotFoundResponseDto;
import com.jjss.civideo.global.exception.dto.UnauthorizedResponseDto;
import com.jjss.civideo.global.exception.dto.UnsupportedMediaTypeResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponseDto handle400(BindException e) {
        return BadRequestResponseDto.of(e);
    }

    @ExceptionHandler(NotFoundDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public NotFoundDataResponseDto handle400(NotFoundDataException e) {
        return NotFoundDataResponseDto.of(e);
    }

    @ExceptionHandler({
            AuthenticationException.class,
            OAuth2AuthorizationRequestMissingException.class,
            ExpiredJwtException.class,
            MalformedJwtException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public UnauthorizedResponseDto handle401(Exception e) {
        return UnauthorizedResponseDto.of(e);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundResponseDto handle404(NoHandlerFoundException e) {
        return NotFoundResponseDto.of(e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public MethodNotAllowedResponseDto handle405(HttpRequestMethodNotSupportedException e) {
        return MethodNotAllowedResponseDto.of(e);
    }

    @ExceptionHandler(ConflictDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ConflictResponseDto handle409(ConflictDataException e) {
        return ConflictResponseDto.of(e);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public UnsupportedMediaTypeResponseDto handle415(HttpMediaTypeNotSupportedException e) {
        return UnsupportedMediaTypeResponseDto.of(e);
    }

}
