package com.jjss.civideo.global.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

	INSUFFICIENT_AUTHENTICATION_EXCEPTION("인증이 필요한 리소스입니다."),
	OAUTH2_AUTHORIZATION_REQUEST_MISSING_EXCEPTION("비정상적인 인증 절차입니다."),
	EXPIRED_JWT_EXCEPTION("만료된 토큰입니다."),
	MALFORMED_JWT_EXCEPTION("올바르게 구성된 JWT Token이 아닙니다."),
	UNSUPPORTED_JWT_EXCEPTION("올바른 형태의 JWT Token이 아닙니다."),
	ILLEGAL_ARGUMENT_EXCEPTION("토큰이 null이거나 비어있을 수 없습니다."),
	HTTP_MESSAGE_NOT_READABLE_EXCEPTION("잘못된 JSON 형식입니다.");

	private final String message;

}
