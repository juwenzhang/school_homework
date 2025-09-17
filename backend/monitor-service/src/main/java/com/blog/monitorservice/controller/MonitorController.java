package com.blog.monitorservice.controller;

import com.blog.monitorservice.alert.AlertService;
import com.blog.monitorservice.alert.model.AlertConfig;
import com.blog.monitorservice.alert.model.AlertMessage;
import com.blog.monitorservice.collector.MetricsRepository;
import com.blog.monitorservice.collector.model.SystemMetrics;
import com.blog.monitorservice.collector.model.ServiceMetrics;
import com.blog.monitorservice.collector.model.JvmMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);
    private final MetricsRepository metricsRepository;
    private final AlertService alertService;

    @Autowired
    public MonitorController(MetricsRepository metricsRepository, AlertService alertService) {
        this.metricsRepository = metricsRepository;
        this.alertService = alertService;
    }

    /**
     * 获取系统指标历史数据
     */
    @GetMapping("/system/metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SystemMetrics>> getSystemMetrics(
            @RequestParam String serverIp,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            long startTimestamp = startTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            long endTimestamp = endTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            List<SystemMetrics> metrics = metricsRepository.getSystemMetricsHistory(serverIp, startTimestamp, endTimestamp);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("获取系统指标失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取最近的系统指标
     */
    @GetMapping("/system/latest")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemMetrics> getLatestSystemMetrics(@RequestParam String serverIp) {
        try {
            SystemMetrics metrics = metricsRepository.getLatestSystemMetrics(serverIp);
            if (metrics == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("获取最近的系统指标失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取系统资源使用情况统计
     */
    @GetMapping("/system/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSystemStats(
            @RequestParam String serverIp,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            long startTimestamp = startTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            long endTimestamp = endTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            Map<String, Object> stats = metricsRepository.getSystemResourceStats(serverIp, startTimestamp, endTimestamp);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("获取系统资源使用情况统计失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取服务列表
     */
    @GetMapping("/services")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getServices() {
        try {
            List<String> services = metricsRepository.getAllServices();
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            logger.error("获取服务列表失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取服务实例列表
     */
    @GetMapping("/services/{serviceName}/instances")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> getServiceInstances(@PathVariable String serviceName) {
        try {
            List<String> instances = metricsRepository.getServiceInstances(serviceName);
            return ResponseEntity.ok(instances);
        } catch (Exception e) {
            logger.error("获取服务实例列表失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取服务指标历史数据
     */
    @GetMapping("/services/{serviceName}/metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ServiceMetrics>> getServiceMetrics(
            @PathVariable String serviceName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            long startTimestamp = startTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            long endTimestamp = endTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            List<ServiceMetrics> metrics = metricsRepository.getServiceMetricsHistory(serviceName, startTimestamp, endTimestamp);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("获取服务指标失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取最近的服务指标
     */
    @GetMapping("/services/{serviceName}/instances/{instanceId}/latest")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServiceMetrics> getLatestServiceMetrics(
            @PathVariable String serviceName,
            @PathVariable String instanceId) {
        try {
            ServiceMetrics metrics = metricsRepository.getLatestServiceMetrics(serviceName, instanceId);
            if (metrics == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("获取最近的服务指标失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取JVM指标历史数据
     */
    @GetMapping("/jvm/metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JvmMetrics>> getJvmMetrics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        try {
            long startTimestamp = startTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            long endTimestamp = endTime.toEpochSecond(ZoneOffset.UTC) * 1000;
            List<JvmMetrics> metrics = metricsRepository.getJvmMetricsHistory(startTimestamp, endTimestamp);
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("获取JVM指标失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取最近的JVM指标
     */
    @GetMapping("/jvm/latest")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JvmMetrics> getLatestJvmMetrics() {
        try {
            JvmMetrics metrics = metricsRepository.getLatestJvmMetrics();
            if (metrics == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(metrics);
        } catch (Exception e) {
            logger.error("获取最近的JVM指标失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取告警配置
     */
    @GetMapping("/alert/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertConfig> getAlertConfig() {
        try {
            AlertConfig config = alertService.getAlertConfig();
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            logger.error("获取告警配置失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 更新告警配置
     */
    @PutMapping("/alert/config")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlertConfig> updateAlertConfig(@RequestBody AlertConfig config) {
        try {
            alertService.updateAlertConfig(config);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            logger.error("更新告警配置失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 启用或禁用告警
     */
    @PutMapping("/alert/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Boolean>> enableAlert(@RequestParam boolean enable) {
        try {
            alertService.enableAlert(enable);
            Map<String, Boolean> response = new HashMap<>();
            response.put("enabled", enable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("启用或禁用告警失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 清理过期的指标数据
     */
    @DeleteMapping("/metrics/cleanup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> cleanupExpiredMetrics(@RequestParam int days) {
        try {
            metricsRepository.cleanupExpiredMetrics(days);
            Map<String, String> response = new HashMap<>();
            response.put("message", "已清理过期的指标数据（保留最近 " + days + " 天的数据）");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("清理过期的指标数据失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 测试告警功能
     */
    @PostMapping("/alert/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> testAlert() {
        try {
            AlertMessage alertMessage = new AlertMessage();
            alertMessage.setAlertKey("test-alert");
            alertMessage.setAlertType(com.blog.monitorservice.alert.model.AlertType.SYSTEM);
            alertMessage.setTitle("测试告警");
            alertMessage.setMessage("这是一条测试告警消息，用于验证告警功能是否正常工作。");
            alertMessage.setSeverity(com.blog.monitorservice.alert.model.Severity.INFO);
            alertMessage.setServerIp("localhost");
            alertMessage.setTimestamp(System.currentTimeMillis());

            alertService.sendAlert(alertMessage);

            Map<String, String> response = new HashMap<>();
            response.put("message", "测试告警已发送");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("发送测试告警失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取监控服务状态
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("status", "UP");
            status.put("timestamp", System.currentTimeMillis());
            status.put("version", "1.0.0");
            
            // 获取一些关键指标
            SystemMetrics systemMetrics = metricsRepository.getLatestSystemMetrics("localhost");
            if (systemMetrics != null) {
                Map<String, Double> systemStatus = new HashMap<>();
                systemStatus.put("cpuUsage", systemMetrics.getCpuUsage());
                systemStatus.put("memoryUsage", systemMetrics.getMemoryUsage());
                systemStatus.put("diskUsage", systemMetrics.getDiskUsage());
                status.put("system", systemStatus);
            }
            
            // 检查告警是否启用
            AlertConfig alertConfig = alertService.getAlertConfig();
            status.put("alertEnabled", alertConfig.isEnabled());
            
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("获取监控服务状态失败", e);
            Map<String, Object> errorStatus = new HashMap<>();
            errorStatus.put("status", "ERROR");
            errorStatus.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorStatus);
        }
    }
}