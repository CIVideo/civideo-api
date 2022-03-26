package com.jjss.civideo.global.exception;

import com.jjss.civideo.global.exception.dto.BadRequestResponseDto;
import com.jjss.civideo.global.exception.dto.MethodNotAllowedResponseDto;
import com.jjss.civideo.global.exception.dto.NotFoundResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BadRequestResponseDto> handle400(BindException e) {
        log.error("BindException", e);
        return new ResponseEntity<>(BadRequestResponseDto.of(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<NotFoundResponseDto> handle404(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException", e);
        return new ResponseEntity<>(NotFoundResponseDto.of(e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<MethodNotAllowedResponseDto> handle405(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        return new ResponseEntity<>(MethodNotAllowedResponseDto.of(e), HttpStatus.METHOD_NOT_ALLOWED);
    }

}
