package com.jjss.civideo.global.exception;

import com.jjss.civideo.domain.user.exception.OAuth2AuthorizationRequestMissingException;
import com.jjss.civideo.global.exception.dto.BadRequestResponseDto;
import com.jjss.civideo.global.exception.dto.MethodNotAllowedResponseDto;
import com.jjss.civideo.global.exception.dto.NotFoundResponseDto;
import com.jjss.civideo.global.exception.dto.UnauthorizedResponseDto;
import com.jjss.civideo.global.exception.dto.UnsupportedMediaTypeResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BadRequestResponseDto> handle400(BindException e) {
        log.error(e.getClass().getSimpleName(), e);
        return new ResponseEntity<>(BadRequestResponseDto.of(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationException.class, OAuth2AuthorizationRequestMissingException.class, ExpiredJwtException.class, MalformedJwtException.class})
    public ResponseEntity<UnauthorizedResponseDto> handle401(Exception e) {
        log.error(e.getClass().getSimpleName(), e);
        return new ResponseEntity<>(UnauthorizedResponseDto.of(e), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<NotFoundResponseDto> handle404(NoHandlerFoundException e) {
        log.error(e.getClass().getSimpleName(), e);
        return new ResponseEntity<>(NotFoundResponseDto.of(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MethodNotAllowedResponseDto> handle405(HttpRequestMethodNotSupportedException e) {
        log.error(e.getClass().getSimpleName(), e);
        return new ResponseEntity<>(MethodNotAllowedResponseDto.of(e), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<UnsupportedMediaTypeResponseDto> handle415(HttpMediaTypeNotSupportedException e) {
        log.error(e.getClass().getSimpleName(), e);
        return new ResponseEntity<>(UnsupportedMediaTypeResponseDto.of(e), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

}
