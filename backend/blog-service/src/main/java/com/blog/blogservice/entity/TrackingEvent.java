package com.blog.blogservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

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
     * 文章ID
     */
    private Long articleId;

    /**
     * IP地址
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * 设备信息
     */
    @Column(length = 255)
    private String userAgent;

    /**
     * 浏览器信息
     */
    @Column(length = 100)
    private String browser;

    /**
     * 操作系统
     */
    @Column(length = 100)
    private String os;

    /**
     * 事件发生时间
     */
    @Column(nullable = false)
    private LocalDateTime eventTime;

    /**
     * 额外属性（JSON格式）
     */
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> properties;
}