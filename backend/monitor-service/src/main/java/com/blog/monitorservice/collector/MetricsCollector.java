package com.blog.monitorservice.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class MetricsCollector {

    private static final Logger logger = LoggerFactory.getLogger(MetricsCollector.class);
    private final MetricsRepository metricsRepository;
    private final DiscoveryClient discoveryClient;
    private final MetricsEndpoint metricsEndpoint;

    // 记录收集次数
    private final AtomicLong collectionCount = new AtomicLong(0);

    @Autowired
    public MetricsCollector(MetricsRepository metricsRepository, 
                          DiscoveryClient discoveryClient, 
                          MetricsEndpoint metricsEndpoint) {
        this.metricsRepository = metricsRepository;
        this.discoveryClient = discoveryClient;
        this.metricsEndpoint = metricsEndpoint;
    }

    /**
     * 定期收集系统指标
     */
    @Scheduled(fixedRateString = "${monitor.metrics.collection.interval:30000}")
    public void collectSystemMetrics() {
        try {
            if (!isCollectionEnabled()) {
                return;
            }

            long count = collectionCount.incrementAndGet();
            if (count % 10 == 0) {
                logger.info("已执行 {} 次指标收集", count);
            }

            // 收集系统CPU、内存、磁盘使用情况
            SystemMetrics systemMetrics = new SystemMetrics();
            systemMetrics.setCpuUsage(getCpuUsage());
            systemMetrics.setMemoryUsage(getMemoryUsage());
            systemMetrics.setDiskUsage(getDiskUsage());
            systemMetrics.setServerIp(getServerIp());
            systemMetrics.setCollectionTime(System.currentTimeMillis());

            // 保存系统指标
            metricsRepository.saveSystemMetrics(systemMetrics);

            // 收集服务实例信息
            collectServiceMetrics();

            // 收集JVM指标
            collectJvmMetrics();

        } catch (Exception e) {
            logger.error("收集系统指标失败", e);
        }
    }

    /**
     * 收集服务实例指标
     */
    private void collectServiceMetrics() {
        try {
            List<String> services = discoveryClient.getServices();
            for (String service : services) {
                List<ServiceInstance> instances = discoveryClient.getInstances(service);
                for (ServiceInstance instance : instances) {
                    ServiceMetrics serviceMetrics = new ServiceMetrics();
                    serviceMetrics.setServiceName(service);
                    serviceMetrics.setInstanceId(instance.getInstanceId());
                    serviceMetrics.setHost(instance.getHost());
                    serviceMetrics.setPort(instance.getPort());
                    serviceMetrics.setUri(instance.getUri().toString());
                    serviceMetrics.setMetadata(instance.getMetadata());
                    serviceMetrics.setCollectionTime(System.currentTimeMillis());

                    // 保存服务指标
                    metricsRepository.saveServiceMetrics(serviceMetrics);
                }
            }
        } catch (Exception e) {
            logger.error("收集服务指标失败", e);
        }
    }

    /**
     * 收集JVM指标
     */
    private void collectJvmMetrics() {
        try {
            // 获取JVM内存使用情况
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            long usedMemory = totalMemory - freeMemory;

            JvmMetrics jvmMetrics = new JvmMetrics();
            jvmMetrics.setTotalMemory(totalMemory);
            jvmMetrics.setFreeMemory(freeMemory);
            jvmMetrics.setUsedMemory(usedMemory);
            jvmMetrics.setMaxMemory(maxMemory);
            jvmMetrics.setThreadCount(Thread.activeCount());
            jvmMetrics.setCollectionTime(System.currentTimeMillis());

            // 从actuator获取更多指标
            try {
                Map<String, Double> metrics = metricsEndpoint.metrics().getNames().stream()
                        .filter(name -> name.startsWith("jvm.") || name.startsWith("http.server.requests"))
                        .collect(java.util.stream.Collectors.toMap(
                                name -> name,
                                name -> {
                                    try {
                                        return metricsEndpoint.metric(name, null)
                                                .getMeasurements().stream()
                                                .findFirst()
                                                .map(MetricsEndpoint.Sample::getValue)
                                                .orElse(0.0);
                                    } catch (Exception e) {
                                        return 0.0;
                                    }
                                }
                        ));
                jvmMetrics.setDetailedMetrics(metrics);
            } catch (Exception e) {
                logger.warn("获取详细指标失败", e);
            }

            // 保存JVM指标
            metricsRepository.saveJvmMetrics(jvmMetrics);
        } catch (Exception e) {
            logger.error("收集JVM指标失败", e);
        }
    }

    /**
     * 获取CPU使用率
     */
    private double getCpuUsage() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                        (com.sun.management.OperatingSystemMXBean) osBean;
                return sunOsBean.getSystemCpuLoad() * 100;
            }
        } catch (Exception e) {
            logger.warn("获取CPU使用率失败", e);
        }
        return 0.0;
    }

    /**
     * 获取内存使用率
     */
    private double getMemoryUsage() {
        try {
            OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
            if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
                com.sun.management.OperatingSystemMXBean sunOsBean = 
                        (com.sun.management.OperatingSystemMXBean) osBean;
                long totalPhysicalMemory = sunOsBean.getTotalPhysicalMemorySize();
                long freePhysicalMemory = sunOsBean.getFreePhysicalMemorySize();
                long usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
                return (double) usedPhysicalMemory / totalPhysicalMemory * 100;
            }
        } catch (Exception e) {
            logger.warn("获取内存使用率失败", e);
        }
        return 0.0;
    }

    /**
     * 获取磁盘使用率
     */
    private double getDiskUsage() {
        try {
            File root = new File("/");
            long totalSpace = root.getTotalSpace();
            long freeSpace = root.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            return (double) usedSpace / totalSpace * 100;
        } catch (Exception e) {
            logger.warn("获取磁盘使用率失败", e);
        }
        return 0.0;
    }

    /**
     * 获取服务器IP地址
     */
    private String getServerIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.warn("获取服务器IP失败", e);
            return "unknown";
        }
    }

    /**
     * 检查指标收集是否启用
     */
    private boolean isCollectionEnabled() {
        return true;
    }
}