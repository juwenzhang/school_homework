package com.blog.blogservice.exception;

import org.springframework.http.HttpStatus;

/**
 * 自定义业务异常类
 */
public class BlogException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus status;

    public BlogException(String message, ErrorCode errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public BlogException(String message, Throwable cause, ErrorCode errorCode, HttpStatus status) {
        super(message, cause);
        this.errorCode = errorCode;
        this.status = status;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}