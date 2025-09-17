package com.blog.common.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 埋点事件实体类
 */
@Data
@Entity
@Table(name = "blog_tracking_event")
public class TrackingEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 事件名称
     */
    @Column(nullable = false, length = 100)
    private String eventName;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 会话ID
     */
    @Column(nullable = false, length = 100)
    private String sessionId;

    /**
     * 页面URL
     */
    private String pageUrl;

    /**
     * 页面标题
     */
    private String pageTitle;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 事件数据(JSON格式)
     */
    @Column(columnDefinition = "jsonb")
    private String eventData;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTime;
}