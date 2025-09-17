package com.blog.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 埋点事件DTO类
 */
@Data
public class TrackingEventDTO {
    private Long id;
    private String eventName;
    private Long userId;
    private String sessionId;
    private String pageUrl;
    private String pageTitle;
    private String userAgent;
    private String ipAddress;
    private String eventData;
    private LocalDateTime createdTime;
}