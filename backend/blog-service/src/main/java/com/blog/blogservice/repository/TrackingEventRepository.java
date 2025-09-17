package com.blog.blogservice.repository;

import com.blog.blogservice.entity.TrackingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 埋点事件仓库接口
 */
@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, Long> {

    /**
     * 根据事件名称查询事件列表
     * @param eventName 事件名称
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> findByEventName(String eventName, Pageable pageable);

    /**
     * 根据用户ID查询事件列表
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据文章ID查询事件列表
     * @param articleId 文章ID
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> findByArticleId(Long articleId, Pageable pageable);

    /**
     * 根据时间范围查询事件列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> findByEventTimeBetween(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 根据事件名称和时间范围查询事件列表
     * @param eventName 事件名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 分页参数
     * @return 事件列表
     */
    Page<TrackingEvent> findByEventNameAndEventTimeBetween(String eventName, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 统计指定时间范围内的事件数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 事件数量
     */
    Long countByEventTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计指定事件名称和时间范围内的事件数量
     * @param eventName 事件名称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 事件数量
     */
    Long countByEventNameAndEventTimeBetween(String eventName, LocalDateTime startTime, LocalDateTime endTime);
}