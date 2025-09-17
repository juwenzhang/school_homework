package com.blog.common.exception;

/**
 * 角色相关异常
 */
public class RoleException extends RuntimeException {

    public RoleException(String message) {
        super(message);
    }

    public RoleException(String message, Throwable cause) {
        super(message, cause);
    }
}