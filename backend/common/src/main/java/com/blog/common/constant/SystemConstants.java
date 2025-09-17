package com.blog.common.constant;

/**
 * 系统常量类
 */
public class SystemConstants {
    // 分页相关常量
    public static final Integer DEFAULT_PAGE_NUM = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 10;
    public static final Integer MAX_PAGE_SIZE = 100;

    // 用户相关常量
    public static final Integer USER_STATUS_ENABLED = 1;
    public static final Integer USER_STATUS_DISABLED = 0;
    public static final String USER_DEFAULT_AVATAR = "https://example.com/default-avatar.png";
    public static final String USER_ROLE_ADMIN = "ADMIN";
    public static final String USER_ROLE_USER = "USER";

    // 文章相关常量
    public static final Integer ARTICLE_STATUS_DRAFT = 0;
    public static final Integer ARTICLE_STATUS_PUBLISHED = 1;
    public static final Integer ARTICLE_IS_TOP_FALSE = 0;
    public static final Integer ARTICLE_IS_TOP_TRUE = 1;
    public static final Integer ARTICLE_ALLOW_COMMENT_FALSE = 0;
    public static final Integer ARTICLE_ALLOW_COMMENT_TRUE = 1;

    // 评论相关常量
    public static final Integer COMMENT_STATUS_PENDING = 0;
    public static final Integer COMMENT_STATUS_APPROVED = 1;
    public static final Integer COMMENT_STATUS_REJECTED = 2;

    // 权限相关常量
    public static final Integer PERMISSION_TYPE_MENU = 1; // 菜单
    public static final Integer PERMISSION_TYPE_BUTTON = 2; // 按钮
    public static final Integer PERMISSION_TYPE_API = 3; // API

    // 缓存相关常量
    public static final String CACHE_KEY_PREFIX = "blog:";
    public static final Long CACHE_EXPIRE_TIME = 3600L;
    public static final String CACHE_KEY_USER = CACHE_KEY_PREFIX + "user:";
    public static final String CACHE_KEY_ARTICLE = CACHE_KEY_PREFIX + "article:";
    public static final String CACHE_KEY_CATEGORY = CACHE_KEY_PREFIX + "category:";
    public static final String CACHE_KEY_TAG = CACHE_KEY_PREFIX + "tag:";
    public static final String CACHE_KEY_PERMISSION = CACHE_KEY_PREFIX + "permission:";
    public static final String CACHE_KEY_ROLE = CACHE_KEY_PREFIX + "role:";

    // JWT相关常量
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_HEADER_KEY = "Authorization";
    public static final String JWT_SECRET_KEY = "your-secret-key";
    public static final Long JWT_EXPIRE_TIME = 7200000L; // 2小时

    // 埋点相关常量
    public static final String TRACKING_EVENT_PAGE_VIEW = "page_view";
    public static final String TRACKING_EVENT_CLICK = "click";
    public static final String TRACKING_EVENT_SEARCH = "search";
    public static final String TRACKING_EVENT_LOGIN = "login";
    public static final String TRACKING_EVENT_REGISTER = "register";
    public static final String TRACKING_EVENT_LOGOUT = "logout";

    // 监控相关常量
    public static final String METRIC_TYPE_CPU = "cpu";
    public static final String METRIC_TYPE_MEMORY = "memory";
    public static final String METRIC_TYPE_DISK = "disk";
    public static final String METRIC_TYPE_NETWORK = "network";
    public static final String METRIC_TYPE_REQUEST = "request";
    public static final String METRIC_TYPE_ERROR = "error";
    public static final String METRIC_TYPE_LATENCY = "latency";
}