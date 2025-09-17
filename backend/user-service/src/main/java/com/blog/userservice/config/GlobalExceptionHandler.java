package com.blog.userservice.config;

import com.blog.common.dto.ResponseDTO;
import com.blog.common.exception.BlogException;
import com.blog.common.exception.UserException;
import com.blog.userservice.exception.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理用户相关异常
     */
    @ExceptionHandler(UserException.class)
    @ResponseBody
    public ResponseDTO<?> handleUserException(UserException e) {
        logger.error("用户异常: {}", e.getMessage(), e);
        return ResponseDTO.fail(e.getStatusCode(), e.getErrorCode(), e.getMessage());
    }

    /**
     * 处理权限相关异常
     */
    @ExceptionHandler(PermissionException.class)
    @ResponseBody
    public ResponseDTO<?> handlePermissionException(PermissionException e) {
        logger.error("权限异常: {}", e.getMessage(), e);
        return ResponseDTO.fail(e.getStatusCode(), e.getErrorCode(), e.getMessage());
    }

    /**
     * 处理业务逻辑异常
     */
    @ExceptionHandler(BlogException.class)
    @ResponseBody
    public ResponseDTO<?> handleBlogException(BlogException e) {
        logger.error("业务异常: {}", e.getMessage(), e);
        return ResponseDTO.fail(e.getStatusCode(), e.getErrorCode(), e.getMessage());
    }

    /**
     * 处理Spring Security认证异常
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseDTO<?> handleBadCredentialsException(BadCredentialsException e) {
        logger.error("认证失败: {}", e.getMessage());
        return ResponseDTO.fail(HttpStatus.UNAUTHORIZED.value(), "AUTH_FAILED", "用户名或密码错误");
    }

    /**
     * 处理禁用用户异常
     */
    @ExceptionHandler(DisabledException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDTO<?> handleDisabledException(DisabledException e) {
        logger.error("用户禁用: {}", e.getMessage());
        return ResponseDTO.fail(HttpStatus.FORBIDDEN.value(), "USER_DISABLED", "用户已被禁用");
    }

    /**
     * 处理用户锁定异常
     */
    @ExceptionHandler(LockedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDTO<?> handleLockedException(LockedException e) {
        logger.error("用户锁定: {}", e.getMessage());
        return ResponseDTO.fail(HttpStatus.FORBIDDEN.value(), "USER_LOCKED", "用户已被锁定");
    }

    /**
     * 处理访问拒绝异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseDTO<?> handleAccessDeniedException(AccessDeniedException e) {
        logger.error("访问拒绝: {}", e.getMessage());
        return ResponseDTO.fail(HttpStatus.FORBIDDEN.value(), "ACCESS_DENIED", "无权限访问");
    }

    /**
     * 处理请求参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error("参数验证异常: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseDTO.fail(HttpStatus.BAD_REQUEST.value(), "PARAM_VALIDATION_FAILED", "参数验证失败", errors);
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("缺少请求参数: {}", e.getMessage());
        return ResponseDTO.fail(HttpStatus.BAD_REQUEST.value(), "MISSING_PARAM", "缺少参数: " + e.getParameterName());
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseDTO<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持的请求方法: {}", e.getMessage());
        return ResponseDTO.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), "METHOD_NOT_ALLOWED", "不支持的请求方法");
    }

    /**
     * 处理请求路径不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        logger.error("请求路径不存在: {}", e.getMessage());
        return ResponseDTO.fail(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "请求路径不存在");
    }

    /**
     * 处理JSON解析异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("JSON解析异常: {}", e.getMessage());
        return ResponseDTO.fail(HttpStatus.BAD_REQUEST.value(), "JSON_PARSE_ERROR", "请求体格式错误");
    }

    /**
     * 处理数据库异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<?> handleSQLException(SQLException e) {
        logger.error("数据库异常: {}", e.getMessage(), e);
        return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DB_ERROR", "数据库操作失败");
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<?> handleException(Exception e, HttpServletRequest request) {
        logger.error("未捕获异常: 请求路径[{}], 异常信息[{}]", request.getRequestURI(), e.getMessage(), e);
        return ResponseDTO.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SERVER_ERROR", "服务器内部错误");
    }
}