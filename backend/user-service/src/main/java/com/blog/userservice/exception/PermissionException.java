package com.blog.userservice.exception;

/**
 * 权限相关异常类
 */
public class PermissionException extends RuntimeException {
    
    private Integer statusCode = 400;
    private String errorCode = "PERMISSION_ERROR";

    public PermissionException(String message) {
        super(message);
    }

    public PermissionException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public PermissionException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PermissionException(String message, Integer statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}