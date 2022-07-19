package com.dambae200.dambae200.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST.value(), "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED.value(), "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "C004", "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST.value(), "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "C006", "Access is Denied"), // Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생

    // User
        LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST.value(), "U001", "Login input is invalid"),
        EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "U002", "Email is Duplication"),
        NICKNAME_DUPLICATION(HttpStatus.BAD_REQUEST.value(), "U003", "Nickname is Duplication"),

    // Store
    INVALID_STORE_BRAND_CODE(HttpStatus.BAD_REQUEST.value(), "S001", "Invalid Store Brand Code"),

    // Access
    INVALID_ACCESS_TYPE_CODE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A001", "Invalid Access Type Code"),
    CANNOT_FIND_ACCESS_NOTIFCATION_TYPE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A002", "Cannot Find Access Notification Type"),
    CANNOT_FIND_ACCESS_SITUATION_TYPE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "A003", "Cannot Find Access Situation Type")
    ;

    private int status;
    private final String code;
    private final String type;

    ErrorCode(final int status, final String code, final String message) {
        this.code = code;
        this.status = status;
        this.type = message;
    }

    public String getType() {
        return this.type;
    }
    public int getStatus() { return this.status; }
    public String getCode() { return this.code; }
}