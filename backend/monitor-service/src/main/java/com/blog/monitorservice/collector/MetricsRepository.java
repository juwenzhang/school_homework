package com.blog.monitorservice.collector;

import java.util.List;
import java.util.Map;

public interface MetricsRepository {

    /**
     * 保存系统指标
     */
    void saveSystemMetrics(SystemMetrics metrics);

    /**
     * 保存服务指标
     */
    void saveServiceMetrics(ServiceMetrics metrics);

    /**
     * 保存JVM指标
     */
    void saveJvmMetrics(JvmMetrics metrics);

    /**
     * 获取系统指标历史数据
     */
    List<SystemMetrics> getSystemMetricsHistory(String serverIp, long startTime, long endTime);

    /**
     * 获取服务指标历史数据
     */
    List<ServiceMetrics> getServiceMetricsHistory(String serviceName, long startTime, long endTime);

    /**
     * 获取JVM指标历史数据
     */
    List<JvmMetrics> getJvmMetricsHistory(long startTime, long endTime);

    /**
     * 获取所有服务列表
     */
    List<String> getAllServices();

    /**
     * 获取服务实例列表
     */
    List<String> getServiceInstances(String serviceName);

    /**
     * 获取系统资源使用情况统计
     */
    Map<String, Object> getSystemResourceStats(String serverIp, long startTime, long endTime);

    /**
     * 清理过期的指标数据
     */
    void cleanupExpiredMetrics(int retentionDays);

    /**
     * 获取最近的系统指标
     */
    SystemMetrics getLatestSystemMetrics(String serverIp);

    /**
     * 获取最近的服务指标
     */
    ServiceMetrics getLatestServiceMetrics(String serviceName, String instanceId);

    /**
     * 获取最近的JVM指标
     */
    JvmMetrics getLatestJvmMetrics();
}