package com.blog.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义业务异常类
 */
@Getter
public class BlogException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    public BlogException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = "BLOG_ERROR";
    }

    public BlogException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = "BLOG_ERROR";
    }

    public BlogException(String message, String errorCode) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode;
    }

    public BlogException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}