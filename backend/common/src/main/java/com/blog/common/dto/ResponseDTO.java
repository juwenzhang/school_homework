package com.blog.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 统一响应DTO
 */
@Data
public class ResponseDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 成功响应
     */
    public static <T> ResponseDTO<T> success(T data) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ResponseDTO<T> success() {
        return success(null);
    }

    /**
     * 失败响应
     */
    public static <T> ResponseDTO<T> fail(Integer code, String message) {
        ResponseDTO<T> response = new ResponseDTO<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    /**
     * 失败响应（默认500）
     */
    public static <T> ResponseDTO<T> fail(String message) {
        return fail(500, message);
    }

    /**
     * 参数错误响应
     */
    public static <T> ResponseDTO<T> badRequest(String message) {
        return fail(400, message);
    }

    /**
     * 未授权响应
     */
    public static <T> ResponseDTO<T> unauthorized(String message) {
        return fail(401, message);
    }

    /**
     * 禁止访问响应
     */
    public static <T> ResponseDTO<T> forbidden(String message) {
        return fail(403, message);
    }

    /**
     * 资源未找到响应
     */
    public static <T> ResponseDTO<T> notFound(String message) {
        return fail(404, message);
    }
}