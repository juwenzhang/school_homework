package com.blog.common.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统监控指标实体类
 */
@Data
@Entity
@Table(name = "blog_system_metric")
public class SystemMetric implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 指标名称
     */
    @Column(nullable = false, length = 100)
    private String metricName;

    /**
     * 指标值
     */
    @Column(nullable = false)
    private Double metricValue;

    /**
     * 指标类型
     */
    @Column(nullable = false, length = 50)
    private String metricType;

    /**
     * 应用名称
     */
    @Column(nullable = false, length = 50)
    private String applicationName;

    /**
     * 实例ID
     */
    @Column(length = 100)
    private String instanceId;

    /**
     * 标签(JSON格式)
     */
    @Column(columnDefinition = "jsonb")
    private String tags;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdTime;
}