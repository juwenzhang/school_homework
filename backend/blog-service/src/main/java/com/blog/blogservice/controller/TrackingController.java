package com.blog.blogservice.controller;

import com.blog.blogservice.entity.TrackingEvent;
import com.blog.blogservice.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 埋点控制器
 */
@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    /**
     * 记录自定义事件
     */
    @PostMapping("/events")
    public ResponseEntity<Void> trackEvent(@RequestParam String eventName,
                                          @RequestParam(required = false) Long userId,
                                          @RequestParam(required = false) Long articleId,
                                          @RequestBody(required = false) Map<String, Object> properties) {
        trackingService.trackEvent(eventName, userId, articleId, properties);
        return ResponseEntity.ok().build();
    }

    /**
     * 记录页面访问事件
     */
    @PostMapping("/page-views")
    public ResponseEntity<Void> trackPageView(@RequestParam String pageUrl,
                                            @RequestParam(required = false) Long userId,
                                            @RequestParam(required = false, defaultValue = "0") Long duration) {
        trackingService.trackPageView(pageUrl, userId, duration);
        return ResponseEntity.ok().build();
    }

    /**
     * 记录文章阅读事件
     */
    @PostMapping("/article-reads")
    public ResponseEntity<Void> trackArticleRead(@RequestParam Long articleId,
                                               @RequestParam(required = false) Long userId,
                                               @RequestParam(required = false, defaultValue = "0") Long duration) {
        trackingService.trackArticleRead(articleId, userId, duration);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据事件名称查询事件列表
     */
    @GetMapping("/events/name/{eventName}")
    public ResponseEntity<Page<TrackingEvent>> getEventsByEventName(@PathVariable String eventName,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrackingEvent> events = trackingService.getEventsByEventName(eventName, pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * 根据用户ID查询事件列表
     */
    @GetMapping("/events/user/{userId}")
    public ResponseEntity<Page<TrackingEvent>> getEventsByUserId(@PathVariable Long userId,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrackingEvent> events = trackingService.getEventsByUserId(userId, pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * 根据文章ID查询事件列表
     */
    @GetMapping("/events/article/{articleId}")
    public ResponseEntity<Page<TrackingEvent>> getEventsByArticleId(@PathVariable Long articleId,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrackingEvent> events = trackingService.getEventsByArticleId(articleId, pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * 根据时间范围查询事件列表
     */
    @GetMapping("/events/time-range")
    public ResponseEntity<Page<TrackingEvent>> getEventsByTimeRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TrackingEvent> events = trackingService.getEventsByTimeRange(startTime, endTime, pageable);
        return ResponseEntity.ok(events);
    }

    /**
     * 统计指定时间范围内的事件数量
     */
    @GetMapping("/stats/events/count")
    public ResponseEntity<Long> countEventsByTimeRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Long count = trackingService.countEventsByTimeRange(startTime, endTime);
        return ResponseEntity.ok(count);
    }

    /**
     * 统计指定事件名称和时间范围内的事件数量
     */
    @GetMapping("/stats/events/count-by-name")
    public ResponseEntity<Long> countEventsByEventNameAndTimeRange(@RequestParam String eventName,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
                                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        Long count = trackingService.countEventsByEventNameAndTimeRange(eventName, startTime, endTime);
        return ResponseEntity.ok(count);
    }

    /**
     * 获取热门文章的访问统计
     */
    @GetMapping("/stats/popular-articles")
    public ResponseEntity<Map<Long, Long>> getPopularArticles(@RequestParam(defaultValue = "10") int limit,
                                                            @RequestParam(defaultValue = "7") int days) {
        Map<Long, Long> stats = trackingService.getPopularArticles(limit, days);
        return ResponseEntity.ok(stats);
    }

    /**
     * 获取用户活跃度统计
     */
    @GetMapping("/stats/user-activity")
    public ResponseEntity<Map<String, Long>> getUserActivity(@RequestParam(defaultValue = "7") int days) {
        Map<String, Long> activity = trackingService.getUserActivity(days);
        return ResponseEntity.ok(activity);
    }
}