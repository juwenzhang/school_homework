package com.blog.blogservice.service;

import com.blog.blogservice.entity.TrackingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 埋点服务接口
 */
public interface TrackingService {

    /**
     * 记录用户行为事件
     * @param eventName 事件名称
     * @param userId 用户ID（可选）
     * @param articleId 文章ID（可选）
     * @param properties 额外属性（可选）
     */
    void trackEvent(String eventName, Long userId, Long articleId, Map<String, Object> properties);

    /**
     * 记录页面访问事件
     * @param pageUrl 页面URL
     * @param userId 用户ID（可选）
     * @param duration 停留时长（秒）
     */
    void trackPageView(String pageUrl, Long userId, Long duration);

    /**
     * 记录文章阅读事件
     * @param articleId 文章ID
     * @param userId 用户ID（可选）
     * @param duration 阅读时长（秒）
     */
    void trackArticleRead(Long articleId, Long userId, Long duration);

    /**
     * 根据事件名称查询事件列表
     * @param eventName 事件名称
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> getEventsByEventName(String eventName, Pageable pageable);

    /**
     * 根据用户ID查询事件列表
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> getEventsByUserId(Long userId, Pageable pageable);

    /**
     * 根据文章ID查询事件列表
     * @param articleId 文章ID
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> getEventsByArticleId(Long articleId, Pageable pageable);

    /**
     * 根据时间范围查询事件列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> getEventsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 统计指定时间范围内的事件数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 事件数量
     */
    Long countEventsByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定事件名称和时间范围内的事件数量
     * @param eventName 事件名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 事件数量
     */
    Long countEventsByEventNameAndTimeRange(String eventName, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取热门文章的访问统计
     * @param limit 返回的文章数量
     * @param days 统计的天数
     * @return 文章访问统计列表
     */
    Map<Long, Long> getPopularArticles(int limit, int days);

    /**
     * 获取用户活跃度统计
     * @param days 统计的天数
     * @return 每日活跃用户数
     */
    Map<String, Long> getUserActivity(int days);
}