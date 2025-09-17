package com.blog.monitorservice.collector.model;

import com.blog.monitorservice.collector.model.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Entity
@Table(name = "jvm_metrics")
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class JvmMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_memory")
    private Long totalMemory;

    @Column(name = "free_memory")
    private Long freeMemory;

    @Column(name = "used_memory")
    private Long usedMemory;

    @Column(name = "max_memory")
    private Long maxMemory;

    @Column(name = "thread_count")
    private Integer threadCount;

    @Type(type = "jsonb")
    @Column(name = "detailed_metrics", columnDefinition = "jsonb")
    private Map<String, Double> detailedMetrics;

    @Column(name = "collection_time", nullable = false)
    private Long collectionTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 构造方法
    public JvmMetrics() {
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(Long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public Long getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(Long usedMemory) {
        this.usedMemory = usedMemory;
    }

    public Long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(Long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    public Map<String, Double> getDetailedMetrics() {
        return detailedMetrics;
    }

    public void setDetailedMetrics(Map<String, Double> detailedMetrics) {
        this.detailedMetrics = detailedMetrics;
    }

    public Long getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(Long collectionTime) {
        this.collectionTime = collectionTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    @Override
    public String toString() {
        return "JvmMetrics{" +
                "id=" + id +
                ", totalMemory=" + totalMemory +
                ", freeMemory=" + freeMemory +
                ", usedMemory=" + usedMemory +
                ", maxMemory=" + maxMemory +
                ", threadCount=" + threadCount +
                ", collectionTime=" + collectionTime +
                '}';
    }
}