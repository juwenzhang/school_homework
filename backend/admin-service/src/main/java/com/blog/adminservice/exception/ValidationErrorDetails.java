package com.blog.adminservice.exception;

import java.util.Date;
import java.util.Map;

public class ValidationErrorDetails extends ErrorDetails {

    private Map<String, String> fieldErrors;

    public ValidationErrorDetails(Date timestamp, String message, String details, String errorCode, Map<String, String> fieldErrors) {
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