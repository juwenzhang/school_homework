package com.blog.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 用户相关异常类
 */
public class UserException extends BlogException {
    private static final String USER_ERROR_CODE = "USER_ERROR";

    public UserException(String message) {
        super(message, USER_ERROR_CODE);
    }

    public UserException(String message, HttpStatus status) {
        super(message, USER_ERROR_CODE, status);
    }

    public UserException(String message, String errorCode) {
        super(message, errorCode);
    }

    public UserException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}