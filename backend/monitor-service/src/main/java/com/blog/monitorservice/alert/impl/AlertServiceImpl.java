package com.blog.monitorservice.alert.impl;

import com.blog.monitorservice.alert.AlertService;
import com.blog.monitorservice.alert.model.AlertConfig;
import com.blog.monitorservice.alert.model.AlertMessage;
import com.blog.monitorservice.alert.model.AlertStatus;
import com.blog.monitorservice.alert.model.AlertType;
import com.blog.monitorservice.alert.model.Severity;
import com.blog.monitorservice.collector.model.SystemMetrics;
import com.blog.monitorservice.collector.model.ServiceMetrics;
import com.blog.monitorservice.collector.model.JvmMetrics;
import com.blog.monitorservice.notification.NotificationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class AlertServiceImpl implements AlertService {

    private static final Logger logger = LoggerFactory.getLogger(AlertServiceImpl.class);
    private final Map<String, AlertStatus> alertStatusMap = new ConcurrentHashMap<>();
    private final AlertConfig alertConfig;
    private boolean alertEnabled;
    private final NotificationManager notificationManager;

    @Value("${monitor.alert.threshold.cpu:80}")
    private int cpuThreshold;

    @Value("${monitor.alert.threshold.memory:80}")
    private int memoryThreshold;

    @Value("${monitor.alert.threshold.disk:90}")
    private int diskThreshold;

    @Value("${monitor.alert.enabled:true}")
    private boolean defaultAlertEnabled;

    @Autowired
    public AlertServiceImpl(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
        this.alertConfig = new AlertConfig();
        this.alertEnabled = defaultAlertEnabled;
    }

    @Override
    public void checkSystemAlert(SystemMetrics metrics) {
        if (!alertEnabled) {
            return;
        }

        try {
            String alertKey = "system-" + metrics.getServerIp();
            boolean shouldAlert = false;
            StringBuilder alertMessage = new StringBuilder();

            // 检查CPU使用率
            if (metrics.getCpuUsage() != null && metrics.getCpuUsage() > cpuThreshold) {
                shouldAlert = true;
                alertMessage.append("CPU使用率过高：").append(metrics.getCpuUsage()).append("% (阈值：").append(cpuThreshold).append("%)\n");
            }

            // 检查内存使用率
            if (metrics.getMemoryUsage() != null && metrics.getMemoryUsage() > memoryThreshold) {
                shouldAlert = true;
                alertMessage.append("内存使用率过高：").append(metrics.getMemoryUsage()).append("% (阈值：").append(memoryThreshold).append("%)\n");
            }

            // 检查磁盘使用率
            if (metrics.getDiskUsage() != null && metrics.getDiskUsage() > diskThreshold) {
                shouldAlert = true;
                alertMessage.append("磁盘使用率过高：").append(metrics.getDiskUsage()).append("% (阈值：").append(diskThreshold).append("%)\n");
            }

            if (shouldAlert && shouldSendAlert(alertKey)) {
                AlertMessage message = new AlertMessage();
                message.setAlertKey(alertKey);
                message.setAlertType(AlertType.SYSTEM);
                message.setTitle("系统资源告警 - " + metrics.getServerIp());
                message.setMessage(alertMessage.toString());
                message.setSeverity(Severity.HIGH);
                message.setServerIp(metrics.getServerIp());
                message.setTimestamp(System.currentTimeMillis());

                sendAlert(message);
            }
        } catch (Exception e) {
            logger.error("检查系统告警失败", e);
        }
    }

    @Override
    public void checkServiceAlert(ServiceMetrics metrics) {
        if (!alertEnabled) {
            return;
        }

        try {
            // 服务告警逻辑
            String alertKey = "service-" + metrics.getServiceName() + "-" + metrics.getInstanceId();
            
            if (metrics.getMetadata() != null) {
                Object healthStatus = metrics.getMetadata().get("healthStatus");
                if (healthStatus != null && "UNHEALTHY".equals(healthStatus.toString())) {
                    if (shouldSendAlert(alertKey)) {
                        AlertMessage message = new AlertMessage();
                        message.setAlertKey(alertKey);
                        message.setAlertType(AlertType.SERVICE);
                        message.setTitle("服务健康告警 - " + metrics.getServiceName());
                        message.setMessage("服务实例 " + metrics.getInstanceId() + " 状态异常\n主机：" + metrics.getHost() + ":" + metrics.getPort());
                        message.setSeverity(Severity.MEDIUM);
                        message.setServiceName(metrics.getServiceName());
                        message.setInstanceId(metrics.getInstanceId());
                        message.setTimestamp(System.currentTimeMillis());

                        sendAlert(message);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("检查服务告警失败", e);
        }
    }

    @Override
    public void checkJvmAlert(JvmMetrics metrics) {
        if (!alertEnabled) {
            return;
        }

        try {
            String alertKey = "jvm-" + metrics.getCollectionTime();
            boolean shouldAlert = false;
            StringBuilder alertMessage = new StringBuilder();

            // 检查JVM内存使用情况
            if (metrics.getUsedMemory() != null && metrics.getMaxMemory() != null) {
                double memoryUsage = (double) metrics.getUsedMemory() / metrics.getMaxMemory() * 100;
                if (memoryUsage > memoryThreshold) {
                    shouldAlert = true;
                    alertMessage.append("JVM内存使用率过高：").append(String.format("%.2f", memoryUsage)).append("% (阈值：").append(memoryThreshold).append("%)\n");
                }
            }

            // 检查线程数量
            if (metrics.getThreadCount() != null && metrics.getThreadCount() > 500) {
                shouldAlert = true;
                alertMessage.append("JVM线程数量过多：").append(metrics.getThreadCount()).append(" (阈值：500)\n");
            }

            if (shouldAlert && shouldSendAlert(alertKey)) {
                AlertMessage message = new AlertMessage();
                message.setAlertKey(alertKey);
                message.setAlertType(AlertType.JVM);
                message.setTitle("JVM资源告警");
                message.setMessage(alertMessage.toString());
                message.setSeverity(Severity.MEDIUM);
                message.setTimestamp(System.currentTimeMillis());

                sendAlert(message);
            }
        } catch (Exception e) {
            logger.error("检查JVM告警失败", e);
        }
    }

    @Override
    public void sendAlert(AlertMessage alertMessage) {
        try {
            logger.warn("发送告警通知：{}", alertMessage.getTitle());
            logger.warn("告警内容：{}", alertMessage.getMessage());

            // 更新告警状态
            AlertStatus status = new AlertStatus();
            status.setAlertKey(alertMessage.getAlertKey());
            status.setLastAlertTime(System.currentTimeMillis());
            status.setAlertCount(1);
            alertStatusMap.put(alertMessage.getAlertKey(), status);

            // 使用通知管理器发送告警通知
            Map<String, Boolean> results = notificationManager.sendNotification(alertMessage);
            
            // 检查是否有至少一个通知渠道发送成功
            boolean anySuccess = results.values().stream().anyMatch(Boolean::booleanValue);
            
            if (anySuccess) {
                logger.info("告警通知发送成功: {}", alertMessage.getAlertKey());
            } else {
                logger.warn("所有通知渠道发送失败: {}", alertMessage.getAlertKey());
            }

        } catch (Exception e) {
            logger.error("发送告警通知失败", e);
        }
    }

    @Override
    public boolean shouldSendAlert(String alertKey) {
        try {
            // 检查告警频率限制，避免告警风暴
            AlertStatus status = alertStatusMap.get(alertKey);
            if (status == null) {
                return true;
            }

            // 距离上次告警的时间
            long timeSinceLastAlert = System.currentTimeMillis() - status.getLastAlertTime();
            
            // 告警频率限制：10分钟内最多发送一次
            long alertInterval = TimeUnit.MINUTES.toMillis(10);
            
            return timeSinceLastAlert > alertInterval;
        } catch (Exception e) {
            logger.error("检查告警频率限制失败", e);
            return true; // 出错时默认允许发送告警
        }
    }

    @Override
    public AlertConfig getAlertConfig() {
        return alertConfig;
    }

    @Override
    public void updateAlertConfig(AlertConfig alertConfig) {
        try {
            // 更新告警配置
            this.alertConfig.setThreshold(alertConfig.getThreshold());
            this.alertConfig.setNotification(alertConfig.getNotification());
            this.alertConfig.setEnabled(alertConfig.isEnabled());
            
            // 更新阈值
            this.cpuThreshold = alertConfig.getThreshold().getCpu();
            this.memoryThreshold = alertConfig.getThreshold().getMemory();
            this.diskThreshold = alertConfig.getThreshold().getDisk();
            
            // 更新告警启用状态
            this.alertEnabled = alertConfig.isEnabled();
            
            logger.info("告警配置已更新");
        } catch (Exception e) {
            logger.error("更新告警配置失败", e);
        }
    }

    @Override
    public void enableAlert(boolean enable) {
        this.alertEnabled = enable;
        this.alertConfig.setEnabled(enable);
        logger.info("告警功能已{}", enable ? "启用" : "禁用");
    }

    @Override
    public AlertStatus getAlertStatus(String alertKey) {
        return alertStatusMap.get(alertKey);
    }
}