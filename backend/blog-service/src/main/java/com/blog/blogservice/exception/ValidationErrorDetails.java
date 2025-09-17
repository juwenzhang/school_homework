package com.blog.blogservice.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 验证错误详情类
 */
public class ValidationErrorDetails extends ErrorDetails {

    private Map<String, String> fieldErrors;

    public ValidationErrorDetails(LocalDateTime timestamp, String message, String details, String errorCode, Map<String, String> fieldErrors) {
        super(timestamp, message, details, errorCode);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}