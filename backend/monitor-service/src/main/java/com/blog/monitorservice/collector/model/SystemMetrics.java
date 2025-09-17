package com.blog.monitorservice.collector.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "system_metrics")
public class SystemMetrics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_ip", nullable = false)
    private String serverIp;

    @Column(name = "cpu_usage", precision = 5, scale = 2)
    private Double cpuUsage;

    @Column(name = "memory_usage", precision = 5, scale = 2)
    private Double memoryUsage;

    @Column(name = "disk_usage", precision = 5, scale = 2)
    private Double diskUsage;

    @Column(name = "collection_time", nullable = false)
    private Long collectionTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 构造方法
    public SystemMetrics() {
        this.createdAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    // getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
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
        return "SystemMetrics{" +
                "id=" + id +
                ", serverIp='" + serverIp + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", diskUsage=" + diskUsage +
                ", collectionTime=" + collectionTime +
                '}';
    }
}