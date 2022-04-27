package com.jjss.civideo.global.config.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

	@Before("execution(* com.jjss.civideo.global.exception.config.GlobalExceptionHandler.*(*))")
	public void before(JoinPoint joinPoint) {
		Exception exception = (Exception) joinPoint.getArgs()[0];
		log.error(exception.getClass().getSimpleName(), exception);
	}

}
