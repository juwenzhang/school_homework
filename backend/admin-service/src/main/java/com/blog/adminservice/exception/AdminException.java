package com.blog.adminservice.exception;

public class AdminException extends RuntimeException {

    private String errorCode;

    public AdminException(String message) {
        super(message);
        this.errorCode = "ADMIN_ERROR";
    }

    public AdminException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AdminException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ADMIN_ERROR";
    }

    public AdminException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}