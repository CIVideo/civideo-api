package com.jsss.civideo.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(400,"C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405,"C002", "Innvalid Method"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access Denied"),

    //Member
    EMAIL_DUPLICATION(400, "M003", "Email Duplication"),
    LOGIN_INPUT_INVALID(400, "M004", "Login Input Invalid"),
    ;

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
