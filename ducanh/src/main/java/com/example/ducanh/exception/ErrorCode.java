package com.example.ducanh.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(999, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "user existed",HttpStatus.BAD_REQUEST),
    INVALID_KEY(1000, "invalid message key",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002,"username must be at least {min} characters",HttpStatus.BAD_REQUEST),
    USERNOTFOUND_EXCEPTION(1004,"user not found",HttpStatus.NOT_FOUND),
    PASSWORD_INVALID(1003,"password must be at least {min} characters",HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006,"unauthenticated",HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1007,"You do not have permission",HttpStatus.FORBIDDEN),
    INVALID_AGE(1008,"Your age must be at least {min}",HttpStatus.FORBIDDEN);


    ErrorCode(Integer code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;


}
