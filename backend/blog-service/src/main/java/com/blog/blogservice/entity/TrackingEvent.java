package com.blog.blogservice.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 埋点事件实体类
 */
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    // toString method
    @Override
    public String toString() {
        return "TrackingEvent{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", userId=" + userId +
                ", articleId=" + articleId +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", browser='" + browser + '\'' +
                ", os='" + os + '\'' +
                ", eventTime=" + eventTime +
                ", properties=" + properties +
                '}';
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackingEvent that = (TrackingEvent) o;
        return Objects.equals(id, that.id);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}