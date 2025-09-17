package com.blog.blogservice.exception;

/**
 * 错误码枚举类
 */
public enum ErrorCode {
    // 通用错误
    INTERNAL_ERROR,              // 内部错误
    VALIDATION_FAILED,           // 验证失败
    ILLEGAL_ARGUMENT,            // 非法参数
    TYPE_MISMATCH,               // 类型不匹配
    METHOD_NOT_SUPPORTED,        // 方法不支持
    RESOURCE_NOT_FOUND,          // 资源不存在
    DATA_INTEGRITY_VIOLATION,    // 数据完整性违反
    
    // 认证授权错误
    AUTHENTICATION_FAILED,       // 认证失败
    ACCESS_DENIED,               // 访问拒绝
    JWT_EXPIRED,                 // JWT过期
    JWT_INVALID,                 // JWT无效
    
    // 业务错误
    ARTICLE_NOT_FOUND,           // 文章不存在
    CATEGORY_NOT_FOUND,          // 分类不存在
    TAG_NOT_FOUND,               // 标签不存在
    COMMENT_NOT_FOUND,           // 评论不存在
    USER_NOT_FOUND,              // 用户不存在
    USER_ALREADY_EXISTS,         // 用户已存在
    CATEGORY_ALREADY_EXISTS,     // 分类已存在
    TAG_ALREADY_EXISTS,          // 标签已存在
    ARTICLE_ALREADY_PUBLISHED,   // 文章已发布
    COMMENT_ALREADY_APPROVED,    // 评论已审核
    FILE_UPLOAD_FAILED,          // 文件上传失败
    RATE_LIMIT_EXCEEDED,         // 超出限流
    
    // 系统错误
    SERVICE_UNAVAILABLE,         // 服务不可用
    DATABASE_ERROR,              // 数据库错误
    NETWORK_ERROR,               // 网络错误
    CACHE_ERROR                  // 缓存错误
}