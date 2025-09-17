package com.blog.monitorservice.collector.impl;

import com.blog.monitorservice.collector.MetricsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JpaMetricsRepository implements MetricsRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaMetricsRepository.class);
    private final EntityManager entityManager;

    @Autowired
    public JpaMetricsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void saveSystemMetrics(SystemMetrics metrics) {
        try {
            entityManager.persist(metrics);
        } catch (Exception e) {
            logger.error("保存系统指标失败", e);
        }
    }

    @Override
    public void saveServiceMetrics(ServiceMetrics metrics) {
        try {
            entityManager.persist(metrics);
        } catch (Exception e) {
            logger.error("保存服务指标失败", e);
        }
    }

    @Override
    public void saveJvmMetrics(JvmMetrics metrics) {
        try {
            entityManager.persist(metrics);
        } catch (Exception e) {
            logger.error("保存JVM指标失败", e);
        }
    }

    @Override
    public List<SystemMetrics> getSystemMetricsHistory(String serverIp, long startTime, long endTime) {
        try {
            String jpql = "SELECT m FROM SystemMetrics m WHERE m.serverIp = :serverIp AND m.collectionTime BETWEEN :startTime AND :endTime ORDER BY m.collectionTime ASC";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("serverIp", serverIp);
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("查询系统指标历史数据失败", e);
            return List.of();
        }
    }

    @Override
    public List<ServiceMetrics> getServiceMetricsHistory(String serviceName, long startTime, long endTime) {
        try {
            String jpql = "SELECT m FROM ServiceMetrics m WHERE m.serviceName = :serviceName AND m.collectionTime BETWEEN :startTime AND :endTime ORDER BY m.collectionTime ASC";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("serviceName", serviceName);
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("查询服务指标历史数据失败", e);
            return List.of();
        }
    }

    @Override
    public List<JvmMetrics> getJvmMetricsHistory(long startTime, long endTime) {
        try {
            String jpql = "SELECT m FROM JvmMetrics m WHERE m.collectionTime BETWEEN :startTime AND :endTime ORDER BY m.collectionTime ASC";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("startTime", startTime);
            query.setParameter("endTime", endTime);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("查询JVM指标历史数据失败", e);
            return List.of();
        }
    }

    @Override
    public List<String> getAllServices() {
        try {
            String jpql = "SELECT DISTINCT m.serviceName FROM ServiceMetrics m ORDER BY m.serviceName";
            return entityManager.createQuery(jpql, String.class).getResultList();
        } catch (Exception e) {
            logger.error("查询所有服务列表失败", e);
            return List.of();
        }
    }

    @Override
    public List<String> getServiceInstances(String serviceName) {
        try {
            String jpql = "SELECT DISTINCT m.instanceId FROM ServiceMetrics m WHERE m.serviceName = :serviceName ORDER BY m.instanceId";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("serviceName", serviceName);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("查询服务实例列表失败", e);
            return List.of();
        }
    }

    @Override
    public Map<String, Object> getSystemResourceStats(String serverIp, long startTime, long endTime) {
        Map<String, Object> stats = new HashMap<>();
        try {
            // 获取CPU使用率统计
            String cpuJpql = "SELECT AVG(m.cpuUsage), MAX(m.cpuUsage), MIN(m.cpuUsage) FROM SystemMetrics m WHERE m.serverIp = :serverIp AND m.collectionTime BETWEEN :startTime AND :endTime";
            Query cpuQuery = entityManager.createQuery(cpuJpql);
            cpuQuery.setParameter("serverIp", serverIp);
            cpuQuery.setParameter("startTime", startTime);
            cpuQuery.setParameter("endTime", endTime);
            Object[] cpuResult = (Object[]) cpuQuery.getSingleResult();
            stats.put("avgCpuUsage", cpuResult[0]);
            stats.put("maxCpuUsage", cpuResult[1]);
            stats.put("minCpuUsage", cpuResult[2]);

            // 获取内存使用率统计
            String memoryJpql = "SELECT AVG(m.memoryUsage), MAX(m.memoryUsage), MIN(m.memoryUsage) FROM SystemMetrics m WHERE m.serverIp = :serverIp AND m.collectionTime BETWEEN :startTime AND :endTime";
            Query memoryQuery = entityManager.createQuery(memoryJpql);
            memoryQuery.setParameter("serverIp", serverIp);
            memoryQuery.setParameter("startTime", startTime);
            memoryQuery.setParameter("endTime", endTime);
            Object[] memoryResult = (Object[]) memoryQuery.getSingleResult();
            stats.put("avgMemoryUsage", memoryResult[0]);
            stats.put("maxMemoryUsage", memoryResult[1]);
            stats.put("minMemoryUsage", memoryResult[2]);

            // 获取磁盘使用率统计
            String diskJpql = "SELECT AVG(m.diskUsage), MAX(m.diskUsage), MIN(m.diskUsage) FROM SystemMetrics m WHERE m.serverIp = :serverIp AND m.collectionTime BETWEEN :startTime AND :endTime";
            Query diskQuery = entityManager.createQuery(diskJpql);
            diskQuery.setParameter("serverIp", serverIp);
            diskQuery.setParameter("startTime", startTime);
            diskQuery.setParameter("endTime", endTime);
            Object[] diskResult = (Object[]) diskQuery.getSingleResult();
            stats.put("avgDiskUsage", diskResult[0]);
            stats.put("maxDiskUsage", diskResult[1]);
            stats.put("minDiskUsage", diskResult[2]);

        } catch (Exception e) {
            logger.error("获取系统资源使用情况统计失败", e);
        }
        return stats;
    }

    @Override
    public void cleanupExpiredMetrics(int retentionDays) {
        try {
            long cutoffTime = System.currentTimeMillis() - (retentionDays * 24L * 60L * 60L * 1000L);

            // 清理过期的系统指标
            String systemJpql = "DELETE FROM SystemMetrics m WHERE m.collectionTime < :cutoffTime";
            Query systemQuery = entityManager.createQuery(systemJpql);
            systemQuery.setParameter("cutoffTime", cutoffTime);
            int systemDeletedCount = systemQuery.executeUpdate();
            logger.info("清理了 {} 条过期的系统指标", systemDeletedCount);

            // 清理过期的服务指标
            String serviceJpql = "DELETE FROM ServiceMetrics m WHERE m.collectionTime < :cutoffTime";
            Query serviceQuery = entityManager.createQuery(serviceJpql);
            serviceQuery.setParameter("cutoffTime", cutoffTime);
            int serviceDeletedCount = serviceQuery.executeUpdate();
            logger.info("清理了 {} 条过期的服务指标", serviceDeletedCount);

            // 清理过期的JVM指标
            String jvmJpql = "DELETE FROM JvmMetrics m WHERE m.collectionTime < :cutoffTime";
            Query jvmQuery = entityManager.createQuery(jvmJpql);
            jvmQuery.setParameter("cutoffTime", cutoffTime);
            int jvmDeletedCount = jvmQuery.executeUpdate();
            logger.info("清理了 {} 条过期的JVM指标", jvmDeletedCount);

        } catch (Exception e) {
            logger.error("清理过期指标数据失败", e);
        }
    }

    @Override
    public SystemMetrics getLatestSystemMetrics(String serverIp) {
        try {
            String jpql = "SELECT m FROM SystemMetrics m WHERE m.serverIp = :serverIp ORDER BY m.collectionTime DESC";
            Query query = entityManager.createQuery(jpql, SystemMetrics.class);
            query.setParameter("serverIp", serverIp);
            query.setMaxResults(1);
            List<SystemMetrics> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            logger.error("获取最近的系统指标失败", e);
            return null;
        }
    }

    @Override
    public ServiceMetrics getLatestServiceMetrics(String serviceName, String instanceId) {
        try {
            String jpql = "SELECT m FROM ServiceMetrics m WHERE m.serviceName = :serviceName AND m.instanceId = :instanceId ORDER BY m.collectionTime DESC";
            Query query = entityManager.createQuery(jpql, ServiceMetrics.class);
            query.setParameter("serviceName", serviceName);
            query.setParameter("instanceId", instanceId);
            query.setMaxResults(1);
            List<ServiceMetrics> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            logger.error("获取最近的服务指标失败", e);
            return null;
        }
    }

    @Override
    public JvmMetrics getLatestJvmMetrics() {
        try {
            String jpql = "SELECT m FROM JvmMetrics m ORDER BY m.collectionTime DESC";
            Query query = entityManager.createQuery(jpql, JvmMetrics.class);
            query.setMaxResults(1);
            List<JvmMetrics> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            logger.error("获取最近的JVM指标失败", e);
            return null;
        }
    }
}