package com.jsss.civideo.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생
     *  httpMessageConverter 에서 등록한 HttpMessageConverter binding 못한 경우 발생
     *  주로 @RequestBody 에서 발생, @RequestPart 어노테이션 발생
     **/
    protected ResponseEntity<ErrorResponse>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        log.error("handleMethodArgumentNotVaildException", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치 않아 binding error 발생
     * 주로 @RequsetParam enum으로 binding 못했을 경우 발생
     *
     * **/
    protected  ResponseEntity<ErrorResponse>
    handleBindException(BindException e){
        log.error("handleBindException", e);
        final ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum
     * **/
}
