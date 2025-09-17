package com.blog.blogservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(BlogException.class)
    public ResponseEntity<?> handleBlogException(BlogException ex, WebRequest request) {
        logger.error("BlogException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                ex.getErrorCode().name());
        return new ResponseEntity<>(errorDetails, ex.getStatus());
    }

    /**
     * 处理实体未找到异常
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        logger.error("NoSuchElementException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "请求的资源不存在",
                request.getDescription(false),
                ErrorCode.RESOURCE_NOT_FOUND.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理数据完整性异常
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("DataIntegrityViolationException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "数据完整性约束违反",
                request.getDescription(false),
                ErrorCode.DATA_INTEGRITY_VIOLATION.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        logger.error("MethodArgumentNotValidException: {}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ValidationErrorDetails errorDetails = new ValidationErrorDetails(
                LocalDateTime.now(),
                "请求参数验证失败",
                request.getDescription(false),
                ErrorCode.VALIDATION_FAILED.name(),
                errors);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.error("MethodArgumentTypeMismatchException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                String.format("参数 '%s' 类型不匹配，期望类型: %s", ex.getName(), ex.getRequiredType().getSimpleName()),
                request.getDescription(false),
                ErrorCode.TYPE_MISMATCH.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.error("AuthenticationException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "认证失败",
                request.getDescription(false),
                ErrorCode.AUTHENTICATION_FAILED.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * 处理访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        logger.error("AccessDeniedException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "没有权限访问此资源",
                request.getDescription(false),
                ErrorCode.ACCESS_DENIED.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    /**
     * 处理方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        logger.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                String.format("不支持的请求方法 '%s'", ex.getMethod()),
                request.getDescription(false),
                ErrorCode.METHOD_NOT_SUPPORTED.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        logger.error("NoHandlerFoundException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                String.format("请求的URL '%s' 不存在", ex.getRequestURL()),
                request.getDescription(false),
                ErrorCode.RESOURCE_NOT_FOUND.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.error("IllegalArgumentException: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                ErrorCode.ILLEGAL_ARGUMENT.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unexpected exception: {}", ex.getMessage(), ex);
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "服务器内部错误",
                request.getDescription(false),
                ErrorCode.INTERNAL_ERROR.name());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}