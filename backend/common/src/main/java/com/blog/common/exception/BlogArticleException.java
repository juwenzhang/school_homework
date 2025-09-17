package com.blog.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 博客文章相关异常类
 */
public class BlogArticleException extends BlogException {
    private static final String ARTICLE_ERROR_CODE = "ARTICLE_ERROR";

    public BlogArticleException(String message) {
        super(message, ARTICLE_ERROR_CODE);
    }

    public BlogArticleException(String message, HttpStatus status) {
        super(message, ARTICLE_ERROR_CODE, status);
    }

    public BlogArticleException(String message, String errorCode) {
        super(message, errorCode);
    }

    public BlogArticleException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}